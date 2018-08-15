verifyCommandAvailability sqlite3
readonly sqlDatabase="server.db"

sqlEvaluate() {
   local -n sqlResponse="${1}"
   local command="${2}"

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
   done < <(sqlite3 -line "${dataDirectory}/${sqlDatabase}" "${command}")

   sqlResponse["count"]="${count}"
} && readonly -f sqlEvaluate

sqlExecute() {
   local command="${1}"

   sqlite3 -separator , "${dataDirectory}/${sqlDatabase}" <<<"${command}"
} && readonly -f sqlExecute

