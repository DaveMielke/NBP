requireScriptLibraries alerts

ipawsCommand_haveAlerts() {
   local identifier

   for identifier
   do
      local file="${identifier}.${alertFileExtension}"

      if [ ! -f "${file}" ]
      then
         writeClientResponse "removeAlert ${identifier}"
      elif ! ipawsIsSentAlert "${identifier}"
      then
         ipawsInsertSentAlert "${identifier}"
      fi
   done
}

