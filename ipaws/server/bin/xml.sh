verifyCommands xpath

xpathGet() {
   local file="${1}"
   local xpath="${2}"

   xpath -q -e "${xpath}" "${file}"
} && readonly -f xpathGet

