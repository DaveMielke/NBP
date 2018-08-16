verifyCommandAvailability inotifywait
includeScriptLibraries cap sql

endServerSession() {
   local command="begin transaction;"
   local table

   for table in areas alerts
   do
      command+=" delete from ${table} where client='${clientReference}';"
   done

   command+=" commit;"
   sqlExecute "${command}"
}

ipawsMonitorAlerts() {
   coproc inotify {
      exec inotifywait --quiet --monitor \
      --format "%T %f % e" --timefmt "%Y-%m-%dT%H:%M:%S" \
      --event moved_to --event moved_from --event delete \
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
               MOVED_TO) ipawsAlertAdded "${identifier}";;
               MOVED_FROM) ipawsAlertRemoved "${identifier}";;
               DELETE) ipawsAlertRemoved "${identifier}";;
               *) logWarning "unhandled inotify event: ${event}: ${file}";;
            esac
         done
      done
   ) &

   eval exec "9<&-"
}

ipawsAlertAdded() {
   local identifier="${1}"

   ipawsHaveAlert "${identifier}" || {
      local file="${identifier}.${alertFileExtension}"
      set -- $(capGetAlertProperty "${file}" area.SAME)
      [ "${#}" -eq 0 ] && return 0

      local areas=""
      local area

      for area
      do
         [ -n "${areas}" ] && areas+=", "
         areas+="'${area}'"
      done

      local count
      sqlCount count areas "client='${clientReference}' and SAME in (${areas})"
      [ "${count}" -eq 0 ] && return 0

      beginClientResponse
      echo "beginAlert ${identifier}"
      echo "endAlert ${identifier}"
      endClientResponse

      ipawsInsertAlert "${identifier}"
   }
}

ipawsAlertRemoved() {
   local identifier="${1}"

   ipawsHaveAlert "${identifier}" && {
      writeClientResponse "removeAlert ${identifier}"
      ipawsDeleteAlert "${identifier}"
   }
}

ipawsHaveAlert() {
   local identifier="${1}"

   sqlCount count alerts "client='${clientReference}' and identifier='${identifier}'"
   (( count == 0 )) && return 1
   return 0
}

ipawsInsertAlert() {
   local identifier="${1}"

   sqlExecute "insert into alerts (client, identifier) values ('${clientReference}', '${identifier}');"
}

ipawsDeleteAlert() {
   local identifier="${1}"

   sqlExecute "delete from alerts where client='${clientReference}' and identifier='${identifier}';"
}

cd "${dataDirectory}"
ipawsHaveAlerts=false
