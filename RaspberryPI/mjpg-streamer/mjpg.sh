export STREAMER_PATH=$HOME/RaspberryPI/mjpg-streamer/mjpg-streamer-experimental
export LD_LIBRARY_PATH=$STREAMER_PATH
#raspistill --nopreview -w 640 -h 480 -q 5 -o /tmp/stream/pic.jpg -tl 10 -t 9999999 -th 0:0:0: &
#$STREAMER_PATH/mjpg_streamer -i "input_raspicam.so -d 200" -o "output_http.so -w $STREAMER_PATH/www -p 8081"
$STREAMER_PATH/mjpg_streamer -i "input_uvc.so -d /dev/video0 -f 25 -y" -o "output_http.so -w $STREAMER_PATH/www -p 8081"
