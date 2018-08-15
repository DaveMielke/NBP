verifyCommandAvailability inotifywait

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

   beginClientResponse
   echo "beginAlert ${identifier}"
   echo "endAlert ${identifier}"
   endClientResponse
}

ipawsAlertRemoved() {
   local identifier="${1}"

   writeClientResponse "removeAlert ${identifier}"
}

cd "${dataDirectory}"
ipawsMonitorAlerts
