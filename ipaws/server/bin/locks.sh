acquireExclusiveLock() {
   local name="${1}"

   exec 9>>"${dataDirectory}/${name}.lock"
   logDebug "acquiring lock: ${name}"
   flock --nonblock --exclusive 9
   logDebug "lock acquired: ${name}"
} && readonly -f acquireExclusiveLock

