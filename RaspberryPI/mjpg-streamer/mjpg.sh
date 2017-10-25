export STREAMER_PATH=$HOME/gitroot/RaspberryPI/mjpg-streamer/mjpg-streamer-experimental
export LD_LIBRARY_PATH=$STREAMER_PATH
$STREAMER_PATH/mjpg_streamer -i "input_uvc.so -d /dev/video0 -f 25 -y" -o "output_http.so -w $STREAMER_PATH/www -p 8001"
