# Onebox Challenge - Miguel Coronel

## Setup:
- Development Language: Java 8 (Amazon Corretto)
- Main Framework: Spring Boot Framework
- Database: H2 Database (in-memory to facilitate challenge checking and testing) 
- Project Management: Maven
- Documentation: Swagger
- Testing: Junit Jupiter

## Workflow:
1. To start the workflow, the user should obtain a new empty cart with id and expiration information inside.
2. After the cart is created, the id could be use for the following requests:
    - GET cart information (including product list and expiration). *
    - UPDATE product list inside the cart. *
    - DELETE cart.
  *This operation will update expiration time (+10min).
3. Every minute, a worker runs to obtain a list of expired carts and then delete each one of them.


## Demonstration:

- GET NEW CART:

RQ:
GET {{service-url}}/api/v1/carts/new

RS:
```
{
    "uuid": "8b557eba-409a-4990-b817-99f6e3fe711b",
    "expireAt": "2023-05-02T15:51:08.208",
    "productList": []
}
```

- GET CART INFORMATION BY ID:

RQ:
GET {{service-url}}/api/v1/carts/8b557eba-409a-4990-b817-99f6e3fe711b

RS:
```
{
    "uuid": "8b557eba-409a-4990-b817-99f6e3fe711b",
    "expireAt": "2023-05-02T15:52:58.385",
    "productList": []
}
```

- UPDATE PRODUCT LIST:

RQ:
PUT {{service-url}}/api/v1/carts/8b557eba-409a-4990-b817-99f6e3fe711b
```
[
    {
        "id": 1,
        "description":"A new product description",
        "amount":12.00
    },
    {
        "id": 2,
        "description":"Another product description",
        "amount": 23.00
    },
    {
        "id": 3,
        "description": "One more product description",
        "amount": 4.00
    }
]
```

RS:
```
{
    "uuid": "297f4e27-f4e4-48f8-8822-6e341c200f14",
    "expireAt": "2023-05-02T13:46:47.946",
    "productList": [
        {
            "id": 2,
            "description": "product description 2",
            "amount": 2.00
        },
        {
            "id": 3,
            "description": "product description 3",
            "amount": 3.00
        },
        {
            "id": 4,
            "description": "product description 4",
            "amount": 4.00
        }
    ]
}
```

- DELETE CART:

RQ:
DELETE {{service-url}}/api/v1/carts/8b557eba-409a-4990-b817-99f6e3fe711b

RS:
CODE 200 - OK


- GET ALL CARTS (FOR DEMO PURPOSE)

RQ:
GET {{service-url}}/api/v1/carts

RS:
```
[
    {
        "uuid": "c5ffc86c-7d67-4656-bbd1-81d22e8da67b",
        "expireAt": "2023-05-02T17:06:35.958",
        "productList": []
    },
    {
        "uuid": "e7f1bd98-50ea-4390-9192-74b7fa144876",
        "expireAt": "2023-05-02T17:06:38.234",
        "productList": []
    },
    {
        "uuid": "f8b5ff7d-c85c-4193-9fa4-5ca25e63a1c0",
        "expireAt": "2023-05-02T17:06:49.075",
        "productList": [
            {
                "id": 1,
                "description": "A new product description",
                "amount": 12.00
            },
            {
                "id": 2,
                "description": "Another product description",
                "amount": 23.00
            },
            {
                "id": 3,
                "description": "One more product description",
                "amount": 4.00
            }
        ]
    },
    {
        "uuid": "1b3496f2-7df8-4b7b-8c10-ec5ce24d3bcb",
        "expireAt": "2023-05-02T17:07:27.747",
        "productList": [
            {
                "id": 4,
                "description": "A new product description",
                "amount": 12.00
            },
            {
                "id": 5,
                "description": "Another product description",
                "amount": 23.00
            },
            {
                "id": 6,
                "description": "One more product description",
                "amount": 4.00
            }
        ]
    }
]
```

- INFO: A postman collection would be attached along with this document to support api check/testing.

## Unit Testing:
- RUNNING mvn test

- RUNNING mvn surefire-report:report

## API Documentation:
URL: {{service-url}}//swagger-ui/
