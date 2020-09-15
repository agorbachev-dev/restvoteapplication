#API specification

## Available Resources
(application deployed at application context `restvoteapp`, examples on `localhost:8080`)
### Restaurants
##### Available methods
* get all restaurants
```bash
 $ curl -s http://localhost:8080/restvoteapp/api/restaurants --user admin1@example.com:admin1
```
* get restaurant
```bash
 $ curl -s http://localhost:8080/restvoteapp/api/restaurants/100007 --user admin1@example.com:admin1
```
* get restaurants with today menus
```bash
 $ curl -s http://localhost:8080/restvoteapp/api/restaurants/with-today-menus --user admin1@example.com:admin1
```
* get today winner
```bash
 $ curl -s http://localhost:8080/restvoteapp/api/restaurants/today-winner --user user1@example.com:user1
```
* create restaurant
> For admin account only.
```bash
 $ curl -s -X POST -d '{"name":"TEST REST"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoteapp/api/admin/restaurants --user admin1@example.com:admin1
```
* update restaurant
> For admin account only.
```bash
 $ curl -s -X PUT -d '{"name":"TEST REST UPDATE"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoteapp/api/admin/restaurants/100007 --user admin1@example.com:admin1
```
### Menus
> All methods for admin account only.
>
##### Available methods
* get all menus
```bash
 $ curl -s http://localhost:8080/restvoteapp/api/admin/menus --user admin1@example.com:admin1
```
* get menu
```bash
 $ curl -s http://localhost:8080/restvoteapp/api/admin/menus/100010 --user admin1@example.com:admin1
```
* create menu 
```bash
 $ curl -s -X POST -d '{"menudate":"2020-09-30","restaurant":{"id":100007},"dishes":"TEST STRING NEW"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoteapp/api/admin/menus --user admin1@example.com:admin1
```
* update menu
```bash
 $ curl -s -X PUT -d '{"menudate":"2020-09-29","restaurant":{"id":100007},"dishes":"TEST STRING UPDATED"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoteapp/api/admin/menus/100020 --user admin1@example.com:admin1
```

### Votes
##### Available methods
* get user vote
```bash
 $ curl -s http://localhost:8080/restvoteapp/api/votes/100013 --user user1@example.com:user1
```
* get user today vote 
```bash
 $ curl -s http://localhost:8080/restvoteapp/api/votes/today --user user1@example.com:user1
```
* create vote for today `due to 11:00AM`
```bash
 $ curl -s -X POST -d '{"restaurant":{"id":100007}}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoteapp/api/votes/today --user admin1@example.com:admin1
```
* update vote for today `due to 11:00AM`
```bash
 $ curl -s -X PUT -d '{"restaurant":{"id":100008}}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restvoteapp/api/votes/today --user admin1@example.com:admin1
```
* delete vote for today `due to 11:00AM`
```bash
 $ curl -s -X DELETE http://localhost:8080/restvoteapp/api/votes/today --user user1@example.com:user1
```