from tkinter import *
from PIL import Image,ImageTk
from googletrans import Translator


root=Tk()
root.title('Translate English')
root.geometry('500x600')
root.iconbitmap(r'C:\WORKSPACE\Python_Google_Trans\ggtrans.ico')



load=Image.open(r'C:\WORKSPACE\Python_Google_Trans\background1.jpg')
render=ImageTk.PhotoImage(load)
img=Label(root,image=render)
img.place(x=0,y=0)

name=Label(root,text="Translator",fg="#FFFFFF",bd=0,bg="#202124")
name.config(font=("Times New Roman",50))  #font chu, co chu
name.pack(pady=10)


box=Text(root,width=28,height=8,font=("ROBOTO",16))
box.pack(pady=20)

button_Frame=Frame(root).pack(side=BOTTOM)

def clear():
    box.delete(1.0,END)
    box1.delete(1.0,END)
def trans():
    ip=box.get(1.0,END)
    print(ip)
    t=Translator()
    a=t.translate(ip,src="vi",dest="en").text
    box1.insert(END,a)

clear_button=Button(button_Frame,text="Clear",font=(("Times New Roman"),10,'bold'),bg='#303030',fg='#FFFFFF',command=clear)
clear_button.place(x=150,y=320)
trans_button=Button(button_Frame,text="Translate",font=(("Times New Roman"),10,'bold'),bg='#303030',fg='#FFFFFF',command=trans)
trans_button.place(x=290,y=320)

box1=Text(root,width=28,height=8,font=("ROBOTO",16))
box1.pack(pady=20)



root.mainloop()