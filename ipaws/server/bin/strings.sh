joinArrayValues()  {
   local -n array="${1}"
   local delimiter="${2}"

   local result=""
   local index

   for index in ${!array[*]}
   do
      [ -z "${result}" ] || result+="${delimiter}"
      result+="${array["${index}"]}"
   done

   echo "${result}"
}

