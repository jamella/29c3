Use this script to measure the performance impact of the random delay
padding that Dan Kaminsky proposed during his 2012 Black Hat talk.

First, create the files that the script downloads and upload it to 
some HTTPS-Server.

$ dd if=/dev/urandom of=1.txt bs=1 count=1
$ dd if=/dev/urandom of=10.txt bs=1 count=10
$ dd if=/dev/urandom of=100.txt bs=1 count=100
$ dd if=/dev/urandom of=1000.txt bs=1 count=1000
$ dd if=/dev/urandom of=10000.txt bs=1 count=10000
$ dd if=/dev/urandom of=100000.txt bs=1 count=100000
$ dd if=/dev/urandom of=1000000.txt bs=1 count=1000000
$ dd if=/dev/urandom of=10000000.txt bs=1 count=10000000

Then, open dakami.py in a text editor and change the variable
'url_base' to a appropriate value (your HTTPS server).

Run dakami.py and save the output.

Then run the following command as root:
$ tc qdisc add dev eth0 root netem delay 3ms 1ms

Run dakami.py again and compare the output with the previous one.