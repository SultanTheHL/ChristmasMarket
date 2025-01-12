import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import axios from 'axios';
import Sidebar from '../Sidebar';
import { User, Mail, Gift } from 'lucide-react';
import { InventoryDialog } from '../InventoryDialog';
import { Button } from "@/components/ui/button";
import { ITEM_NAMES, Item } from '@/lib/constants';
import { TradePopup } from '../TradePopup';

interface Customer {
  id: number;
  name: string;
  email: string;
}

const CustomerCard = ({ customer }: { customer: Customer }) => {
  const [showTradeMenu, setShowTradeMenu] = useState(false);

  const handleTradeRequest = async (item: Item, quantity: number, money: number) => {
    try {
      const params = new URLSearchParams(); 
      params.append('recipientEmail', customer.email);
      params.append('item', ITEM_NAMES[item]);
      params.append('quantity', quantity.toString());
      params.append('price', money.toString());
      
      const response = await axios.post('https://christmasmarket.onrender.com/customer-item/request-trade', params, {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        withCredentials: true,
      });
      alert(response.data);
      setShowTradeMenu(false);
    } catch (err) {
      console.error('Failed to send trade request:', err);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.9 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.3 }}
      className="bg-white/10 backdrop-blur-sm p-6 rounded-xl border border-red-800/30 shadow-xl hover:border-red-400/50 transition-all duration-300"
    >
      <div className="flex items-start space-x-4">
        <div className="p-3 bg-red-500/20 rounded-full">
          <User className="w-6 h-6 text-yellow-400" />
        </div>
        <div className="flex-1">
          <h3 className="text-xl font-semibold text-white mb-2">{customer.name}</h3>
          <div className="flex items-center text-gray-300 text-sm mb-4">
            <Mail className="w-4 h-4 mr-2" />
            {customer.email}
          </div>
          <div className="flex space-x-3">
            <Button 
              onClick={() => setShowTradeMenu(true)}
              variant="outline"
              className="flex items-center gap-2"
            >
              <Gift className="w-4 h-4" />
              Trade
            </Button>
            <InventoryDialog customerEmail={customer.email} />
            <TradePopup 
              isOpen={showTradeMenu}
              onClose={() => setShowTradeMenu(false)}
              onAccept={handleTradeRequest}
            />
          </div>
        </div>
      </div>
    </motion.div>
  );
};

export function Customers() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [currentUserEmail, setCurrentUserEmail] = useState<string>('');

  useEffect(() => {
    const fetchCurrentUser = async () => {
      try {
        const response = await axios.get<{ email: string }>('https://christmasmarket.onrender.com/auth/profile', {
          withCredentials: true
        });
        setCurrentUserEmail(response.data.email);
      } catch (err) {
        console.error('Failed to fetch current user:', err);
      }
    };

    fetchCurrentUser();
  }, []);

  useEffect(() => {
    const fetchCustomers = async () => {
      try {
        const response = await axios.get<Customer[]>('https://christmasmarket.onrender.com/customers', {
          withCredentials: true
        });
        const filteredCustomers = response.data.filter(
          (customer: Customer) => customer.email !== currentUserEmail
        );
        setCustomers(filteredCustomers);
        setLoading(false);
      } catch (err) {
        console.error('Failed to fetch customers:', err);
        setError('Failed to load customers. Please try again later.');
        setLoading(false);
      }
    };

    if (currentUserEmail) {
      fetchCustomers();
    }
  }, [currentUserEmail]);

  const filteredCustomers = customers.filter(customer => 
    customer.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    customer.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-red-900 to-green-900">
      <Sidebar />
      <div className="container mx-auto px-4 py-8 pt-24">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8 }}
        >
          <h1 className="text-4xl font-bold text-center text-white mb-8">
            Christmas Market Community
          </h1>

          <div className="max-w-md mx-auto mb-8">
            <input
              type="text"
              placeholder="Search customers..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full px-4 py-2 rounded-lg bg-white/10 border border-red-800/30 text-white placeholder-gray-400 focus:outline-none focus:border-red-400/50 transition-all duration-300"
            />
          </div>
          
          {loading ? (
            <div className="text-center text-white">Loading customers...</div>
          ) : error ? (
            <div className="text-center text-red-400 bg-red-900/50 p-4 rounded-lg">
              {error}
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 max-w-6xl mx-auto">
              {filteredCustomers.map((customer) => (
                <CustomerCard key={customer.id} customer={customer} />
              ))}
              {filteredCustomers.length === 0 && (
                <div className="col-span-full text-center text-gray-300">
                  {searchTerm ? 'No matching customers found' : 'No customers found'}
                </div>
              )}
            </div>
          )}
        </motion.div>
      </div>
    </div>
  );
}

export default Customers; 