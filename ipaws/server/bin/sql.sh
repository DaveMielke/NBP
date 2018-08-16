verifyCommandAvailability sqlite3
readonly sqlDatabaseFile="sqlite.db"
readonly sqlConfigurationFile="sqlite.conf"

sqlMakeCommand() {
   sqlCommand=(sqlite3 -init "${configurationDirectory}/${sqlConfigurationFile}" "${@}" "${dataDirectory}/${sqlDatabaseFile}")
} && readonly -f sqlMakeCommand

sqlExecute() {
   local command="${1}"

   sqlMakeCommand -separator ,
   "${sqlCommand[@]}" <<<"${command}"
} && readonly -f sqlExecute

sqlEvaluate() {
   local -n sqlResponse="${1}"
   local command="${2}"

   sqlMakeCommand -line
   sqlCommand+=("${command}")
   local count=0 found=false field equals value

   while read -r field equals value
   do
      [ -z "${field}" ] && {
         found=false
         continue
      }

      "${found}" || {
         found=true
         let count+=1
      }

      [ "${equals}" = "=" ] || continue
      sqlResponse["${count},${field}"]="${value}"
   done < <("${sqlCommand[@]}")

   sqlResponse["count"]="${count}"
} && readonly -f sqlEvaluate

sqlCount() {
   local -n sqlCount="${1}"
   local sqlTable="${2}"
   local sqlWhere="${3}"

   local command="select count(*) as count from ${sqlTable}"
   [ -n "${sqlWhere}" ] && command+=" where ${sqlWhere}"
   command+=";"

   local -A sqlResult
   sqlEvaluate sqlResult "${command}"
   sqlCount="${sqlResult["1,count"]}"
} && readonly -f sqlCount

