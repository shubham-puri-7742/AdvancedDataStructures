#include <iostream>
#include <string>
#include <unordered_map>

using namespace std;

// INVENTORY SYSTEM
// Uses an unordered map (name (string) -> data (Item))
// O(1) access, inserts, and deletes, except when chaining (hopefully rare)

class Item
{
    string name;
    float price;
    uint32_t quantity;
public:
    Item()
    {
        name = "";
        price = 0.0f;
        quantity = 0;
    }
    void add_data();
    void edit_data();
    void display_data();
    string get_name() { return name; }
    float get_price() const { return price; }
    uint32_t get_quantity() const { return quantity; }
    void set_quantity(const uint32_t& new_quantity) { quantity = new_quantity; }
};

void Item::add_data() {
    cin.ignore();
    cout << "\nEnter item name: "; getline(cin, name);
    cout << "Enter quantity: "; cin >> quantity;
    cout << "Enter price: "; cin >> price;
}

void Item::edit_data() {
    cout << "\nEnter new item name: "; getline(cin, name);
    cout << "Enter new quantity: "; cin >> quantity;
    cout << "Enter new price: "; cin >> price;
}

void Item::display_data() {
    cout << "\nItem name: " << name;
    cout << "\nQuantity: " << quantity;
    cout << "\nPrice: " << price;
}

class Inventory
{
    unordered_map<string, Item> items;
    float total_money = 0.0f;
    // no longer used as an index, but the member is retained for listing everything
    int item_count = 0;
    string item_to_check;
    // void remove_item(int);
public:
    void add_item();
    void sell_item();
    void sell_item(const string&);
    void find_item();
    void edit_item();
    void list_items();
    // iterators: since we have an unordered map under the hood
    auto begin() { return items.begin(); }
    auto end() { return items.end(); }
    auto cbegin() const { return items.begin(); }
    auto cend() const { return items.end(); }
    auto begin() const { return items.begin(); }
    auto end() const { return items.end(); }
};

void Inventory::add_item() {
    Item i; i.add_data();
    items[i.get_name()] = i;
    ++item_count;
}

void Inventory::sell_item() {
    cin.ignore();
    cout << "\nEnter item name: "; getline(cin, item_to_check);
    if (items.find(item_to_check) == items.end()) {
        cout << "\nThis item is not in your Inventory";
    }
    else {
        sell_item(item_to_check);
    }
}

void Inventory::sell_item(const string& item_name) {
    uint32_t input_quantity;
    cout << "\nEnter number of items to sell: "; cin >> input_quantity;

    uint32_t quantity = items[item_name].get_quantity();
    if (input_quantity <= quantity) {
        float price = items[item_name].get_price();
        float money_earned = price * input_quantity;
        items[item_name].set_quantity(quantity - input_quantity);
        if (!items[item_name].get_quantity()) {
            items.erase(item_name);
        }
        cout << "\nItems sold sucessfully";
        cout << "\nMoney received: " << money_earned;
        total_money += money_earned;
    }
    else {
        cout << "\nCannot sell more items than you have.";
    }
}

void Inventory::find_item() {
    cin.ignore();
    cout << "\nEnter item name: "; getline(cin, item_to_check);

    if (items.find(item_to_check) == items.end()) {
        cout << "\nThis item is not in your Inventory";
        return;
    }
    cout << "\nItem found";
    items[item_to_check].display_data();
}

void Inventory::edit_item() {
    cin.ignore();
    cout << "\nEnter item name: "; getline(cin, item_to_check);

    if (items.find(item_to_check) == items.end()) {
        cout << "\nThis item is not in your Inventory";
        return;
    }
    cout << "\nItem found";
    items[item_to_check].edit_data();

    // housekeeping (shifting to a new key)
    items[items[item_to_check].get_name()] = items[item_to_check];
    items.erase(item_to_check);
}

void Inventory::list_items() {
    if (!item_count) {
        cout << "\nInventory empty.";
        return;
    }
    for (auto& i : items) {
        i.second.display_data();
        cout << "\n";
    }
}

int main()
{
    int choice;
    Inventory inventory_system;

    while (true)
    {
        cout << "\n\nMENU"
             << "\n1. Add new item"
             << "\n2. Sell item"
             << "\n3. Find item"
             << "\n4. Edit item"
             << "\n5. List items"
             << "\n6. Exit"
             << "\n\nEnter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                inventory_system.add_item();
                break;

            case 2:
                inventory_system.sell_item();
                break;

            case 3:
                inventory_system.find_item();
                break;

            case 4:
                inventory_system.edit_item();
                break;

            case 5:
                inventory_system.list_items();
                break;

            case 6:
                exit(0);

            default:
                cout << "\nInvalid choice entered";
                cin.clear();
                cin.ignore(INT_MAX, '\n');
                break;
        }
    }
}
