
import urllib2
import datetime

url_base = 'https://localhost/' 
files = [ '1.txt' 
        , '10.txt'
        , '100.txt'
        , '1000.txt'
        , '10000.txt'
        , '100000.txt'
        , '1000000.txt'
        , '10000000.txt'
        ]

for file in files:
        url = "%s%s" % (url_base, file)
        # print "doing %s" % url
        times = []
        for i in range(0, 100):
		start = datetime.datetime.now()

		req = urllib2.Request(url)
		f = urllib2.urlopen(req)
		lines = f.readlines()

		end = datetime.datetime.now()

                times.append((end-start).total_seconds())
                # print ".",

        result = 0.0
        for time in times:
                result += time
        result /= len(times)
        print "%s --> %f" % (file, result)

