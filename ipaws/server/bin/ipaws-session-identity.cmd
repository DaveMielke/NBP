ipawsSession_identity() {
   local serialNumber="${1}"
   local apiLevel="${2}"
   local modelName="${3}"

   command="begin transaction;"
   command+=" insert into current_sessions (client, serial, api, model) values ('${clientReference}', '${serialNumber}', '${apiLevel}', '${modelName}');"
   command+=" commit;"
   sqlExecute "${command}"
}

