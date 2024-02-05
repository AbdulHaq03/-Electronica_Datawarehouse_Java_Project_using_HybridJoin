-- OLAP QUERIES

-- Q1 Present total sales of all products supplied by each supplier with respect to quarter and month using drill down concept.
select distinct `sales`.Supplier_ID as Supplier_ID, P.Product_ID as Product_ID, P.Product_Name as Product_Name, T.Quarter as `Quarter`, T.Month as `Month`, SUM(`sales`.Sale) as Total_Sales
from `electronica-dw`.`sales`
join `electronica-dw`.`products` P on `sales`.product_ID = P.product_ID
join `electronica-dw`.`Supplier` Supplier on `sales`.Supplier_ID = Supplier.Supplier_ID
join `electronica-dw`.`Time` T on `sales`.Time_ID = T.Time_ID
group by P.Product_ID, `sales`.Supplier_ID, P.Product_Name, T.Quarter, T.Month
order by P.Product_ID ASC;

-- Q2 Find total sales of product with respect to month using feature of rollup on month and
-- feature of dicing on supplier with name "DJI" and Year as "2019". You will use the
-- grouping sets feature to achieve rollup. Your output should be sequentially ordered
-- according to product and month.
select  S.Product_ID, sup.Supplier_Name, P.Product_Name, T.Month, SUM(S.sale) as Total_sales
from  `electronica-dw`.`sales` S 
inner join `electronica-dw`.`Products` P on S.Product_ID = P.Product_ID
inner join `electronica-dw`.`Time` T on S.Time_ID = T.Time_ID
inner join `electronica-dw`.`Supplier` sup on S.Supplier_ID = sup.Supplier_ID
where sup.Supplier_Name = "DJI" AND T.year = 2019
group by S.Product_ID, T.Month, sup.Supplier_Name, P.Product_Name WITH ROLLUP
order by S.Product_ID, T.Month ASC;

-- Q3 Find the 5 most popular products sold over the weekends.
select P.Product_Name, P.Product_ID,  SUM(S.Sale) as Total_Sales
from `electronica-dw`.`sales` S
inner join `electronica-dw`.`Products` P on S.Product_ID = P.Product_ID
inner join `electronica-dw`.`Time` T on S.Time_ID = T.Time_ID
where T.week in (1, 4)
group by P.Product_ID, P.Product_Name
order by Total_Sales DESC
limit 5;

-- Q4 Present the quarterly sales of each product for 2019 along with its total yearly sales.
-- Note: each quarter sale must be a column and yearly sale as well. Order result according
-- to product
select P.Product_Name, P.Product_ID, SUM(IF(T.Quarter = 1, S.Sale, 0)) as `Quarter_1 Sales`, SUM(IF(T.Quarter = 2, S.Sale, 0)) as `Quarter_2 Sales`,
SUM(IF(T.Quarter = 3, S.Sale, 0)) as `Quarter_3 Sales`, SUM(IF(T.Quarter = 4, S.Sale, 0)) as `Quarter_4 Sales`, SUM(S.sale) as Total_Yearly_Sales
from `electronica-dw`.`sales` S
inner join `electronica-dw`.`Products` P on S.Product_ID = P.Product_ID
inner join `electronica-dw`.`Time` T on S.Time_ID = T.Time_ID
where T.year = 2019
group by P.Product_Name, P.Product_ID
order by P.Product_ID ASC;

-- Q5 ANOMALY DETECTED
-- The anomoly wasn't inserted by my hybrid join but it is present when I was loading the csvs
select * from `electronica-dw`.`Time` where `Time`.year != 2019;

-- Q6 Create a materialised view with the name “STOREANALYSIS_MV” that presents the
-- product-wise sales analysis for each store.
drop view if exists STOREANALYSIS_MV;
Create view STOREANALYSIS_MV as
select Sto.Store_Name as STORE_NAME, Sto.Store_ID as STORE_ID, P.Product_ID as PROD_ID, SUM(S.Sale) as STORE_TOTAL
from `electronica-dw`.`Sales` S 
inner join `electronica-dw`.`Products` P on S.Product_ID = P.Product_ID
inner join `electronica-dw`.`Store` Sto on S.Store_ID = Sto.Store_ID
group by Sto.Store_Name, Sto.Store_ID, P.Product_ID;

select * from STOREANALYSIS_MV;

-- Q7 Use the concept of Slicing calculate the total sales for the store “Tech Haven”and product
-- combination over the months.
select P.Product_Name as PROD_NAME, P.Product_ID as PROD_ID, Sto.Store_Name as STORE_NAME, T.Month as MONTH, SUM(S.sale) as TOTAL_SALES
from `electronica-dw`.`Sales` S
inner join `electronica-dw`.`Products` P on S.Product_ID = P.Product_ID
inner join `electronica-dw`.`Time` T on S.Time_ID = T.Time_ID
inner join `electronica-dw`.`Store` Sto on S.Store_ID = Sto.Store_ID
where Sto.Store_Name = "Tech Haven"
group by P.Product_Name, P.Product_ID, T.Month
order by T.Month;

-- Q8 Create a materialized view named "SUPPLIER_PERFORMANCE_MV" that presents the
-- monthly performance of each supplier.
drop view if exists SUPPLIER_PERFORMANCE_MV;
create view SUPPLIER_PERFORMANCE_MV as
select Sup.Supplier_ID as SUP_ID, T.Month as MONTH, SUM(S.sale) as TOTAL_SALE
from `electronica-dw`.`Sales` S 
inner join `electronica-dw`.`Supplier` Sup on S.Supplier_ID = Sup.Supplier_ID
inner join `electronica-dw`.`Time` T on S.Time_ID = T.Time_ID
group by Sup.Supplier_ID, T.Month
order by Sup.Supplier_ID;

select * from SUPPLIER_PERFORMANCE_MV;

-- Q9 Identify the top 5 customers with the highest total sales in 2019, considering the number
-- of unique products they purchased.
select C.Customer_Name as NAME, C.Customer_ID as CUST_ID, COUNT(DISTINCT P.Product_ID) as PROD_PURCHASED, SUM(S.sale) as TOTAL_SALE
from `electronica-dw`.`Sales` S
inner join `electronica-dw`.`Products` P on S.Product_ID = P.Product_ID
inner join `electronica-dw`.`Customers` C on S.Customer_ID = C.Customer_ID
inner join `electronica-dw`.`Time` T on S.Time_ID = T.Time_ID
where T.year = 2019
group by C.Customer_Name, C.Customer_ID
Order by TOTAL_SALE DESC
limit 5;

-- Q10 Create a materialized view named "CUSTOMER_STORE_SALES_MV" that presents the
-- monthly sales analysis for each store and then customers wise.
drop view if exists CUSTOMER_STORE_SALES_MV;
create view CUSTOMER_STORE_SALES_MV as
select C.Customer_Name as NAME, C.Customer_ID as CUST_ID, Sto.Store_ID as STORE_ID, Sto.Store_Name as STORE_NAME, T.MONTH as MONTH, SUM(S.Sale) as TOTAL_SALES
from `electronica-dw`.`sales` S
inner join `electronica-dw`.`Customers` C on S.Customer_ID = C.Customer_ID
inner join `electronica-dw`.`Store` Sto on S.Store_ID = Sto.Store_ID
inner join `electronica-dw`.`Time` T on S.Time_ID = T.Time_ID
group by C.Customer_Name, C.Customer_ID, Sto.Store_ID, Sto.Store_Name, T.Month
order by C.Customer_ID ASC; 

select * from CUSTOMER_STORE_SALES_MV;

use transactions_data;
select * from transactions;

use `electronica-dw`;
select * from sales;
select * from customers;
select * from products;
select * from store;
select * from supplier;
select * from time;

select count(*) from sales;