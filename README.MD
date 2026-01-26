# SellersApp

Campaign management application for sellers to manage their product campaigns.

## Features

- **Campaign Management**: Create, edit, and delete campaigns for different products
- **Keyword System**: Pre-populated keywords with typeahead suggestions
- **Account Balance**: Track Emerald account balance with automatic deductions
- **Campaign Configuration**:
  - Campaign name (mandatory)
  - Keywords selection with typeahead (mandatory)
  - Bid amount (mandatory)
  - Campaign fund with balance validation (mandatory)
  - Status (ON/OFF)
  - Town selection from dropdown
  - Radius in kilometers (mandatory)

## Technologies

- **Backend**: Spring Boot, Java
- **Database**: H2 (in-memory)
- **Frontend**: HTML, JavaScript, CSS
- **Build Tool**: Maven

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Open browser and go to: `http://localhost:8080`

### Initial Account Balance

- Default Emerald account balance: **$10,000.00**
- Balance resets on application restart
- Balance persists during page refresh

## API Endpoints

### Campaigns

- `GET /Controller/Campaigns` - Get all campaigns
- `GET /Controller/Campaigns/{campaignName}` - Get campaign by name
- `GET /Controller/Campaigns/key/{keyword}` - Get campaigns by keyword
- `POST /Controller/Campaigns` - Create new campaign
- `PUT /Controller/Campaigns/{campaignName}` - Update campaign
- `DELETE /Controller/Campaigns/{campaignName}` - Delete campaign
- `GET /Controller/Campaigns/statuson` - Get all active campaigns

### Keywords

- `GET /Controller/Campaigns/keywords` - Get all pre-populated keywords
- `GET /Controller/Campaigns/keywords/suggest?keyword={keyword}` - Get keyword suggestions

### Balance

- `GET /Controller/balance` - Get current account balance
- `POST /Controller/balance/deduct` - Deduct amount from balance

## Usage

1. **View Balance**: Current Emerald account balance is displayed at the top
2. **Create Campaign**: Fill in all mandatory fields and click "Save"
   - Campaign fund will be deducted from your balance
   - Cannot create campaign if insufficient funds
3. **Edit Campaign**: Click "Edit" button on any campaign to modify it
   - Editing does not affect balance
4. **Delete Campaign**: Click "Delete" to remove campaign
   - Campaign fund is returned to your balance
5. **Keywords**: Start typing to see suggestions, click to add, click cross to remove


