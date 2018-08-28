ipawsSession_identity() {
   local serialNumber="${1}"
   local apiLevel="${2}"
   local modelName="${3}"

   local command="begin transaction;"
   command+=" update current_sessions set serial='${serialNumber}', api='${apiLevel}', model='${modelName}' where client='${clientReference}';"
   command+=" commit;"
   sqlExecute "${command}"
}

