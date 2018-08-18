includeScriptLibraries sql

ipawsSession_getStates() {
   local response="allStates"
   local -A result

   sqlEvaluate result "
   select abbr, SAME, name from states order by name;
   "

   local count="${result["count"]}"
   local index=0

   while ((index < count))
   do
      ((index)) && response+=","
      let index+=1

      response+=" ${result["${index},abbr"]}"
      response+=" ${result["${index},SAME"]}"
      response+=" ${result["${index},name"]}"
   done

   writeClientResponse "${response}"
}

