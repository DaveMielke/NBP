verifyCommandAvailability inotifywait
includeScriptLibraries cap sql

beginServerSession() {
   ipawsKeepAlive
   ipawsPrepareClientTables
} && readonly -f beginServerSession

endServerSession() {
   ipawsPrepareClientTables
} && readonly -f endServerSession

ipawsKeepAlive() {
   local interval=$((30 * 60))

   while sleep "${interval}"
   do
      writeClientResponse "ping $((++pingNumber))"
   done &
} && readonly -f ipawsKeepAlive

ipawsPrepareClientTables() {
   local command="begin transaction;"
   local table

   for table in requested_areas sent_alerts
   do
      command+=" delete from ${table} where client='${clientReference}';"
   done

   command+=" commit;"
   sqlExecute "${command}"
} && readonly -f ipawsPrepareClientTables

ipawsSyncAlerts() {
   set -- *".${alertFileExtension}"
   local file

   for file
   do
      local identifier="${file%.*}"

      if ipawsIsRequestedAlert "${identifier}"
      then
         ipawsIsSentAlert "${identifier}" || ipawsSendAlert "${identifier}"
      else
         ipawsIsSentAlert "${identifier}" && ipawsCancelAlert "${identifier}"
      fi
   done
} && readonly -f ipawsSyncAlerts

ipawsMonitorAlerts() {
   coproc inotify {
      exec inotifywait --quiet --monitor \
      --format "%T %f % e" --timefmt "%Y-%m-%dT%H:%M:%S" \
      --event create --event delete \
      --event moved_from --event moved_to \
      -- .
   }

   local pid="${inotify_PID}"
   pushOnExitCommand kill -TERM "${pid}"
   eval exec "9<&${inotify[0]}"

   (
      ipawsSyncAlerts
      local time file events

      while read -u 9 -r time file events
      do
         [ "${file}" = "${file%.${alertFileExtension}}" ] && continue
         local identifier="${file%.*}"
         local event

         for event in ${events}
         do
            case "${event}"
            in
               CREATE) ipawsAlertAdded "${identifier}";;
               DELETE) ipawsAlertRemoved "${identifier}";;

               MOVED_FROM) ipawsAlertRemoved "${identifier}";;
               MOVED_TO) ipawsAlertAdded "${identifier}";;

               *) logWarning "unhandled inotify event: ${event}: ${file}";;
            esac
         done
      done
   ) &

   eval exec "9<&-"
} && readonly -f ipawsMonitorAlerts

ipawsAlertAdded() {
   local identifier="${1}"

   ipawsIsSentAlert "${identifier}" || {
      ipawsIsRequestedAlert "${identifier}" && {
         ipawsSendAlert "${identifier}"
      }
   }
} && readonly -f ipawsAlertAdded

ipawsAlertRemoved() {
   local identifier="${1}"

   ipawsIsSentAlert "${identifier}" && ipawsCancelAlert "${identifier}"
} && readonly -f ipawsAlertRemoved

ipawsSendAlert() {
   local identifier="${1}"

   logInfo "sending alert: ${clientReference}: ${identifier}"
   beginClientResponse
   echo "beginAlert ${identifier}"
   cat "${identifier}.${alertFileExtension}"
   echo "endAlert ${identifier}"
   endClientResponse
   ipawsInsertSentAlert "${identifier}"
} && readonly -f ipawsSendAlert

ipawsCancelAlert() {
   local identifier="${1}"

   logInfo "cancelling alert: ${clientReference}: ${identifier}"
   writeClientResponse "removeAlert ${identifier}"
   ipawsDeleteSentAlert "${identifier}"
} && readonly -f ipawsCancelAlert

ipawsIsRequestedAlert() {
   local identifier="${1}"

   local file="${identifier}.${alertFileExtension}"
   local areas="$(capGetAlertProperty "${file}" SQL.areas)"
   [ -n "${areas}" ] || return 1

   local count
   sqlCount count requested_areas "client='${clientReference}' and SAME in (${areas})"
   [ "${count}" -eq 0 ] && return 1

   return 0
} && readonly -f ipawsIsRequestedAlert

ipawsIsSentAlert() {
   local identifier="${1}"

   sqlCount count sent_alerts "client='${clientReference}' and identifier='${identifier}'"
   (( count == 0 )) && return 1
   return 0
} && readonly -f ipawsIsSentAlert

ipawsInsertSentAlert() {
   local identifier="${1}"

   sqlExecute "insert into sent_alerts (client, identifier) values ('${clientReference}', '${identifier}');"
} && readonly -f ipawsInsertSentAlert

ipawsDeleteSentAlert() {
   local identifier="${1}"

   sqlExecute "delete from sent_alerts where client='${clientReference}' and identifier='${identifier}';"
} && readonly -f ipawsDeleteSentAlert

cd "${dataDirectory}"
ipawsSendingAlerts=false
pingNumber=0
