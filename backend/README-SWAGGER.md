# Swagger Documentation for the API

## Overview
This project now includes Swagger/OpenAPI documentation for all API endpoints. The documentation provides detailed information about each endpoint, including:

- Endpoint URL and HTTP method
- Request parameters and body schema
- Response codes and body schema
- Data models used by the API

## How to Access the Swagger UI
When the application is running, you can access the Swagger UI at:

```
http://localhost:8080/swagger-ui/index.html
```

This interactive UI allows you to:
- Browse all available endpoints
- See detailed documentation for each endpoint
- Test endpoints directly from the browser
- View model schemas

## Implementation Details
The following changes were made to implement the Swagger documentation:

1. Added the SpringDoc OpenAPI dependency to the project:
   ```xml
   <dependency>
       <groupId>org.springdoc</groupId>
       <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
       <version>2.4.0</version>
   </dependency>
   ```

2. Created a configuration class (`OpenApiConfig.java`) to customize the OpenAPI documentation:
   - Set API information (title, description, version)
   - Added contact information
   - Configured server URL

3. Added annotations to model classes:
   - `@Schema` annotations to describe models and their fields
   - Added example values for better documentation

4. Added annotations to controller classes:
   - `@Tag` annotations to group operations
   - `@Operation` annotations to describe each endpoint
   - `@ApiResponse` annotations to document response codes
   - `@Parameter` annotations to document request parameters

## Available API Groups
The API is organized into the following groups:

1. **Product** - Endpoints for managing products
   - Create, read, update, delete products
   - Get products grouped by category

2. **Order** - Endpoints for managing orders
   - Create, read, update, delete orders
   - Get orders by product ID