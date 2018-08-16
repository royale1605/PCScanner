

#!/bin/bash
find /root/Desktop -type f -mtime -15 -printf '%TY-%Tm-%Td %TT %p\n' | sort -r
