import * as React from 'react'
import { DollarSign, Bell, RotateCw } from 'lucide-react'
import axios from 'axios';
import { useState, useEffect } from 'react';
import './Sidebar.css'
import { Button } from "@/components/ui/button"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { ResponseTradePopup } from './ResponseTradePopup'
import { CartPopup } from './components/CartPopup'

interface TradeRequest {
  id: number;
  trader: {
    id: number;
    email: string;
  };
  recipient: {
    id: number;
    email: string;
  };
  item: {
    id: number;
    name: string;
    basePrice: number;
  storable: boolean;
  item_type: 'GLUHWEIN' | 'APPLE_PIE' | 'SANTA_TOY';
  };
  quantity: number;
  price: number;
  timestamp: string;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED';
}


const Sidebar: React.FC = () => {
  const [user, setUser] = useState<{ id: number; name: string; email: string, wallet: number } | null>(null);
  const [error, setError] = useState("");
  const [isOpen, setIsOpen] = useState(false);
  const [tradeRequests, setTradeRequests] = useState<TradeRequest[]>([]);
  const [selectedRequest, setSelectedRequest] = useState<TradeRequest | null>(null);
  const [isTradePopupOpen, setIsTradePopupOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const handleFetchUser = async () => {
    try {
      const response = await axios.get<{ id: number; name: string; email: string; wallet: number }>(
        `https://christmasmarket.onrender.com/auth/profile`,
        { withCredentials: true }
      );
      setUser(response.data);
    } catch (err: any) {
      setError(err.response?.data || "Failed to fetch profile.");
    }
  }

  const fetchTradeRequests = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get<TradeRequest[]>('https://christmasmarket.onrender.com/customer-item/get-trades', {
        withCredentials: true,
      });
      setTradeRequests(response.data);
    } catch (err) {
      console.error('Failed to fetch trade requests:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const fetchTradeDetails = async (request: TradeRequest) => {
    console.log(request.item);
    try {
      const itemResponse = await axios.get<{ name: string }>(`https://christmasmarket.onrender.com/items/${request.item.id}`, {
        withCredentials: true,
      });
      console.log(itemResponse.data);
      
      setSelectedRequest({
        ...request,
        item: {
          ...request.item,
          name: itemResponse.data.name 
        }
      });
      setIsTradePopupOpen(true);
    } catch (err) {
      console.error('Failed to fetch trade details:', err);
    }
  };

  useEffect(() => {
    handleFetchUser();
    fetchTradeRequests();
  }, []);

  useEffect(() => {
    handleFetchUser();
    
    const handleWalletUpdate = () => {
      handleFetchUser(); 
    };

    window.addEventListener('wallet-update', handleWalletUpdate);
    window.addEventListener('cart-checkout', handleWalletUpdate);
    
    return () => {
      window.removeEventListener('wallet-update', handleWalletUpdate);
      window.removeEventListener('cart-checkout', handleWalletUpdate);
    };
  }, []);

  const handleTradeResponse = async (request: TradeRequest, accepted: boolean) => {
    if (!request || !request.trader || !request.recipient || !request.item) {
      console.error('Invalid trade request data');
      return;
    }
    const params = new URLSearchParams(); 
      params.append('status', accepted ? 'ACCEPTED' : 'DECLINED');

    try {
      

      if (accepted) {
        const response = await axios.post(
          `https://christmasmarket.onrender.com/customer-item/trade/${request.trader.id}/${request.item.id}`,
          null,
          {
            params: {
              quantity: request.quantity,
              price: request.price
            },
            withCredentials: true
          }
        );
        window.alert(response.data);
      }
      
      
      const response = await axios.post(
        `https://christmasmarket.onrender.com/customer-item/close-trade/${request.id}`,
        params,
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          withCredentials: true
        }
      );
      
      window.alert(response.data);
      
      
      setTradeRequests(prev => prev.filter(req => req.id !== request.id));
      setIsTradePopupOpen(false);
      setSelectedRequest(null);
    } catch (err) {
      window.alert('You do not own the specified item: ' + request.item.name);
      await axios.post(
        `https://christmasmarket.onrender.com/customer-item/close-trade/${request.id}`,
        params,
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          withCredentials: true
        }
      );
      setTradeRequests(prev => prev.filter(req => req.id !== request.id));
      setIsTradePopupOpen(false);
      setSelectedRequest(null);
    }
  };

  const handleNotificationClick = (request: TradeRequest) => {
    if (!request || !request.id) {
      console.error('Invalid request data');
      return;
    }
    fetchTradeDetails(request);
    setIsOpen(false);
  };

  if (error) return <p style={{ color: "red" }}>{error}</p>;
  if (!user) return <p>Loading...</p>;
  
  return (
    <header className="header">
      <div className="header-container">
        <div className="left-section">
          <div className="avatar-container">
            <img src="/placeholder.svg?height=32&width=32" alt={user.name} className="avatar-image" />
          </div>
          <div className="greeting">Hi, {user.name}</div>
          <div className="balance-container">
            <DollarSign className="balance-icon" />
            <span className="balance-text">{user.wallet.toFixed(2)}</span>
          </div>
        </div>
        <div className="right-section flex items-center gap-2">
          <CartPopup />
          <Popover open={isOpen} onOpenChange={setIsOpen}>
            <PopoverTrigger asChild>
              <Button 
                variant="outline" 
                size="icon"
                className="relative"
                aria-label="Trade Requests"
              >
                <Bell className="h-[1.2rem] w-[1.2rem]" />
                {tradeRequests.length > 0 && (
                  <span className="absolute top-0 right-0 h-2 w-2 rounded-full bg-red-500" />
                )}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-80 p-0" align="end">
              <div className="flex justify-between items-center py-2 px-4 bg-primary text-primary-foreground">
                <span className="text-sm font-medium">Trade Requests</span>
                <Button 
                  variant="ghost" 
                  size="icon" 
                  onClick={(e) => {
                    e.stopPropagation();
                    fetchTradeRequests();
                  }}
                  disabled={isLoading}
                  className="h-8 w-8 text-primary-foreground hover:text-primary-foreground/80"
                >
                  <RotateCw className={`h-4 w-4 ${isLoading ? 'animate-spin' : ''}`} />
                </Button>
              </div>
              <ul className="divide-y divide-gray-200 max-h-[300px] overflow-auto">
                {tradeRequests.map((request) => (
                  <li 
                    key={request.id} 
                    className="p-4 hover:bg-gray-50 cursor-pointer"
                    onClick={() => handleNotificationClick(request)}
                  >
                    <div className="text-sm font-medium text-gray-900">
                      Trade request from {request.trader.email}
                    </div>
                    <div className="text-xs text-gray-500 mt-1">
                      {new Date(request.timestamp).toLocaleString()}
                    </div>
                  </li>
                ))}
              </ul>
              {tradeRequests.length === 0 && (
                <div className="p-4 text-center text-sm text-gray-500">
                  No pending trade requests
                </div>
              )}
            </PopoverContent>
          </Popover>
        </div>
      </div>

      {selectedRequest && (
        <ResponseTradePopup
          isOpen={isTradePopupOpen}
          onClose={() => {
            setIsTradePopupOpen(false);
            setSelectedRequest(null);
          }}
          onAccept={() => handleTradeResponse(selectedRequest, true)}
          onDecline={() => handleTradeResponse(selectedRequest, false)}
          item={selectedRequest.item}
          quantity={selectedRequest.quantity}
          money={selectedRequest.price}
          buyerEmail={selectedRequest.trader.email}
        />
      )}
    </header>
  );
};

export default Sidebar;
