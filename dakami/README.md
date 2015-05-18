Use this script to measure the performance impact of the random delay
padding that Dan Kaminsky proposed during his 2012 Black Hat talk (https://youtu.be/x0j_HdpNXa0?t=7m12s).

First, create the files that the script downloads and upload it to 
the DocumentRoot of that Linux HTTPS-Server in your lab.

```bash
$ dd if=/dev/urandom of=1.txt bs=1 count=1
$ dd if=/dev/urandom of=10.txt bs=1 count=10
$ dd if=/dev/urandom of=100.txt bs=1 count=100
$ dd if=/dev/urandom of=1000.txt bs=1 count=1000
$ dd if=/dev/urandom of=10000.txt bs=1 count=10000
$ dd if=/dev/urandom of=100000.txt bs=1 count=100000
$ dd if=/dev/urandom of=1000000.txt bs=1 count=1000000
$ dd if=/dev/urandom of=10000000.txt bs=1 count=10000000
```

Then, open `dakami.py` in a text editor and change the variable
`url_base` to an appropriate value (your HTTPS server).

Run `dakami.py` and save the output.

Then run the following command as root on your Linux HTTPS server:

```bash
$ tc qdisc add dev eth0 root netem delay 3ms 1ms
```

Run `dakami.py` again and compare the output with the previous one.
