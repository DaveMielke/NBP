verifyCommands xpath

xmlEvaluate() {
   local file="${1}"
   local xpath="${2}"

   xpath -q -e "${xpath}" "${file}"
} && readonly -f xmlEvaluate

xmlNodeExists() {
   local xml="${1}"
   local xpath="${2}"

   local count="$(xmlEvaluate "${xml}" "count(${xpath})")"
   [ -n "${count}" ] && [ "${count}" -gt 0 ] || return 1
} && readonly -f xmlNodeExists

