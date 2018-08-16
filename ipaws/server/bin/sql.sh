verifyCommandAvailability sqlite3
readonly sqlDatabaseFile="sqlite.db"
readonly sqlConfigurationFile="sqlite.conf"

sqlMakeCommand() {
   sqlCommand=(sqlite3 -init "${configurationDirectory}/${sqlConfigurationFile}" "${@}" "${dataDirectory}/${sqlDatabaseFile}")
} && readonly -f sqlMakeCommand

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

sqlExecute() {
   local command="${1}"

   sqlMakeCommand -separator ,
   "${sqlCommand[@]}" <<<"${command}"
} && readonly -f sqlExecute

