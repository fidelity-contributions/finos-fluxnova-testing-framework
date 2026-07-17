package unit

import java.text.SimpleDateFormat

def date = new Date()
def sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

return sdf2.format(date)