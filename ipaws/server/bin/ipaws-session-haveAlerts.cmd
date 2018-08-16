includeScriptLibraries sql

ipawsSession_haveAlerts() {
   "${ipawsHaveAlerts}" || {
      ipawsHaveAlerts=true
      local identifier

      for identifier
      do
         ipawsInsertAlert "${identifier}"
      done

      ipawsMonitorAlerts
   }
}

