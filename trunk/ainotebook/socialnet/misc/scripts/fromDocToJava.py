###########################################################
# Python Script
# Process data file, strip lines, replace space
###########################################################

if __name__ == '__main__':
	
        print "Running"       
        f = open("data.txt")
        arr = f.readlines()
        for x in arr:
                z = x.strip()
                if z:
                        a = z                        
                        print '    .append("%s")' % a
                                           
        print "Done"    
        
# End   

