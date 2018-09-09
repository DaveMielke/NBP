ipawsCommand_identity() {
   local serialNumber="${1}"
   local apiLevel="${2}"
   local modelName="${3}"

   sqlBegin
   sqlAppend "update current_sessions"
   sqlAppend "set serial='${serialNumber}', api='${apiLevel}', model='${modelName}'"
   sqlAppend "where client='${clientReference}';"
   sqlEnd
}

