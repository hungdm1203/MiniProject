import googletrans
from googletrans import Translator
# print(googletrans.LANGUAGES)
t=Translator()
a=t.translate("xin chao",src="vi",dest="en").text
print(a)