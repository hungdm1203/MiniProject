create database qlnv;
drop database qlnv;
use qlnv;
create table NhanVien(
	maNV varchar(20) primary key,
	hoTen varchar(225),
	gioiTinh varchar(20),
	ngaySinh varchar(20),
	sdt varchar(20),
	email varchar(50),
	diaChi varchar(100),
	phongBan varchar(50),
	chucVu varchar(50),
	luongCB int
);

-- insert into qlnv.NhanVien(maNV,hoTen,gioiTinh,ngaySinh,sdt,email,diachi,phongBan,chucVu,luongCB)
-- values
-- 	('GD','Do Quang Nam','Nam','20/10/1973','0986542173','nam2010@gmail.com','Thanh Xuan-Ha Noi','Giam doc','Giam doc',800000),
-- 	('PGD01','Nguyen Van Thanh','Nam','18/08/1980','0741852963','thanh1808@gmail.com','Hoang Mai-Ha Noi','Giam doc','Pho giam doc',750000),
--     ('PGD02','Tran Thi Yen','Nu','06/06/1983','0147258369','yen0606@gmail.com','Ha Dong-Ha Noi','Giam doc','Pho giam doc',780000),
--     ('TP01','Nguyen Manh Son','Nam','12/03/1980','0987654321','nguyenmanhson@gmail.com','Nam Tu Liem-Ha Noi','Ki thuat','Truong phong',700000),
--     ('TP02','Do Trung Kien','Nam','17/06/1996','0393784239','dotrungkien@gmail.com','Thanh Xuan-Ha Noi','Nhan su','Truong phong',700000),
--     ('PP01','Nguyen Van An','Nam','12/11/1988','0123456789','an12@gmail.com','Ha Dong-Ha Noi','Nhan su','Pho phong',600000),
--     ('NV01','Nguyen Kim Lan','Nu','11/06/1998','0987465123','lan1106@gmail.com','Dong Da-Ha Noi','Nhan su','Nhan vien',600000),
--     ('NV02','Nguyen Thi Thu','Nu','26/06/1990','0246813579','thu2606@gmail.com','Chuong My-Ha Noi','Ki thuat','Nhan vien',500000),
--     ('NV03','Le Van Cong','Nam','20/11/1993','0135792468','cong2011@gmail.com','Ba Dinh-Ha Noi','Ki thuat','Nhan vien',480000),
-- 	 ('NV04','Tran Thi Mai','Nu','06/12/1990','0258963741','mai0612@gmail.com','Hoan Kiem-Ha Noi','Nhan su','Nhan vien',400000);
	
SELECT * FROM qlnv.nhanvien;
