#!/bin/sh
echo " :fire: FIRE UP :fire:"
sleep 20
export NODE_ENV=production

sleep 10
echo " "
echo "<<<<<<<<<<< :recycle: Starting Admin Portal :recycle: >>>>>>>>>>>>>"
echo " "
npm start --host 0.0.0.0

exit 0
