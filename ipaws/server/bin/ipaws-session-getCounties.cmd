includeScriptLibraries sql

ipawsSession_getCounties() {
   local response="stateCounties"

   [ "${#}" -ge 1 ] && {
      local state="${1}"
      local -A result

      sqlEvaluate result "
      select SAME, name from counties where state = '${state}' order by name;
      "

      response+=" ${state}"
      local count="${result["count"]}"
      local index=0

      while ((index < count))
      do
         ((index)) && response+=","
         let index+=1

         response+=" ${result["${index},SAME"]}"
         response+=" ${result["${index},name"]}"
      done
   }

   writeClientResponse "${response}"
}

