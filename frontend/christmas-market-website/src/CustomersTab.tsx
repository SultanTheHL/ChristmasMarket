import React from 'react';
import './CustomersTab.css';
import Sidebar from './Sidebar';
import { TradePopup } from './TradePopup';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { ITEM_NAMES, Item } from '@/lib/constants'
import { InventoryDialog } from './InventoryDialog'

interface Customer {
    id: string;
    name: string;
    email: string;
}

const ITEMS_PER_PAGE = 12;

const CustomersTab: React.FC = () => {
    const [customers, setCustomers] = useState<Customer[]>([])
    const [currentPage, setCurrentPage] = useState(1)
    const [error, setError] = useState("")
    const [isTradePopupOpen, setIsTradePopupOpen] = useState(false)
    const [searchQuery, setSearchQuery] = useState("")
    const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(null);
    
    const handleFetchCustomers = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/customers`, {
                withCredentials: true,
            })
            setCustomers(response.data)
        } catch (err) {
            setError("Failed to fetch customers")
            console.error(err)
        }
    }

    useEffect(() => {
        handleFetchCustomers();
    }, []);

    const filteredCustomers = customers.filter(customer =>
        customer.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    const totalPages = Math.ceil(filteredCustomers.length / ITEMS_PER_PAGE);
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const endIndex = startIndex + ITEMS_PER_PAGE;
    const currentCustomers = filteredCustomers.slice(startIndex, endIndex);

    useEffect(() => {
        setCurrentPage(1);
    }, [searchQuery]);

    const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSearchQuery(e.target.value);
    };

    const clearSearch = () => {
        setSearchQuery("");
    };

    const getPageNumbers = () => {
        const pages = [];
        for (let i = 1; i <= totalPages; i++) {
            pages.push(i);
        }
        return pages;
    };

    const handleTradeClick = (customer: Customer) => {
        setSelectedCustomer(customer);
        setIsTradePopupOpen(true);
    };

    const handleTradeRequest = async (item: Item, quantity: number, money: number) => {
        if (!selectedCustomer) return;

        try {
            const params = new URLSearchParams(); 
            params.append('recipientEmail', selectedCustomer.email);
            params.append('item', ITEM_NAMES[item]);
            params.append('quantity', quantity.toString());
            params.append('price', money.toString());
            const response = await axios.post('http://localhost:8080/customer-item/request-trade', params, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                withCredentials: true,
            });
            alert(response.data);
            setIsTradePopupOpen(false);
            setSelectedCustomer(null);
        } catch (err) {
            console.error('Failed to send trade request:', err);
        }
    };

    return (
        <main className="main-content">
            <Sidebar />
            <div className="search-container">
                <input 
                    type="text" 
                    placeholder="Search by name" 
                    className="search-input"
                    value={searchQuery}
                    onChange={handleSearch}
                />
                {searchQuery && (
                    <button 
                        className="search-clear"
                        onClick={clearSearch}
                    >
                        ✖
                    </button>
                )}
            </div>
            <div className="item-grid">
                {currentCustomers.map((customer) => (
                    <div className="item-row" key={customer.id}>
                        <div className="item-info">
                            <img 
                                src="/placeholder.svg?height=32&width=32" 
                                alt={customer.name} 
                                className="item-avatar" 
                            />
                            <div>
                                <h4 className="item-title">{customer.name}</h4>
                                <p className="item-description">{customer.email}</p>
                            </div>
                        </div>
                        <div className="item-actions">
                            <button 
                                className="action-button" 
                                onClick={() => handleTradeClick(customer)}
                            >
                                Trade
                            </button>
                            <InventoryDialog customerEmail={customer.email} />
                        </div>
                    </div>
                ))}
            </div>
            <TradePopup 
                isOpen={isTradePopupOpen} 
                onClose={() => {
                    setIsTradePopupOpen(false);
                    setSelectedCustomer(null);
                }}
                onAccept={(item, quantity, money) => {
                    handleTradeRequest(item, quantity, money);
                }}
            />
            <div className="pagination">
                <button 
                    className="pagination-button"
                    onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                    disabled={currentPage === 1}
                >
                    ← Previous
                </button>
                {getPageNumbers().map(pageNum => (
                    <button
                        key={pageNum}
                        className={`pagination-button ${pageNum === currentPage ? 'active' : ''}`}
                        onClick={() => setCurrentPage(pageNum)}
                    >
                        {pageNum}
                    </button>
                ))}
                <button 
                    className="pagination-button"
                    onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                    disabled={currentPage === totalPages}
                >
                    Next →
                </button>
            </div>
        </main>
    );
};

export default CustomersTab;
