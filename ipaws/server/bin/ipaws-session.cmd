verifyCommandAvailability inotifywait
includeScriptLibraries cap sql

beginServerSession() {
   emptySessionSpecificTables
} && readonly -f beginServerSession

endServerSession() {
   emptySessionSpecificTables
} && readonly -f endServerSession

emptySessionSpecificTables() {
   local command="begin transaction;"
   local table

   for table in requested_areas sent_alerts
   do
      command+=" delete from ${table} where client='${clientReference}';"
   done

   command+=" commit;"
   sqlExecute "${command}"
} && readonly -f emptySessionSpecificTables

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
      local time file events
      set -- *".${alertFileExtension}"

      for file
      do
         ipawsAlertAdded "${file%.*}"
      done

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

   ipawsHasAlert "${identifier}" || {
      local file="${identifier}.${alertFileExtension}"
      local areas="$(capGetAlertProperty "${file}" SQL.areas)"
      [ -n "${areas}" ] || return 0

      local count
      sqlCount count requested_areas "client='${clientReference}' and SAME in (${areas})"
      [ "${count}" -eq 0 ] && return 0

      beginClientResponse
      echo "beginAlert ${identifier}"
      cat "${file}"
      echo "endAlert ${identifier}"
      endClientResponse

      ipawsInsertAlert "${identifier}"
   }
} && readonly -f ipawsAlertAdded

ipawsAlertRemoved() {
   local identifier="${1}"

   ipawsHasAlert "${identifier}" && {
      writeClientResponse "removeAlert ${identifier}"
      ipawsDeleteAlert "${identifier}"
   }
} && readonly -f ipawsAlertRemoved

ipawsHasAlert() {
   local identifier="${1}"

   sqlCount count sent_alerts "client='${clientReference}' and identifier='${identifier}'"
   (( count == 0 )) && return 1
   return 0
} && readonly -f ipawsHasAlert

ipawsInsertAlert() {
   local identifier="${1}"

   sqlExecute "insert into sent_alerts (client, identifier) values ('${clientReference}', '${identifier}');"
} && readonly -f ipawsInsertAlert

ipawsDeleteAlert() {
   local identifier="${1}"

   sqlExecute "delete from sent_alerts where client='${clientReference}' and identifier='${identifier}';"
} && readonly -f ipawsDeleteAlert

cd "${dataDirectory}"
ipawsSendingAlerts=false
