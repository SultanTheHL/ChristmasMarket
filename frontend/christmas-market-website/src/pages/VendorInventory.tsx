import { useState, useEffect } from 'react';
import { Wine, PieChart, Gift } from 'lucide-react';
import axios from 'axios';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import Sidebar from '../Sidebar';

interface VendorItem {
  id: number;
  vendor: {
    id: number;
    email: string;
  };
  item: {
    id: number;
    name: string;
    item_type: string;
    basePrice: number;
  };
  stock: number;
  customPrice: number;
}

const itemIcons = {
  'Gluhwein': Wine,
  'Classic Apple Pie': PieChart,
  'Santa Action Figure': Gift,
} as const;

function InventoryItem({ item, onPriceChange }: { 
  item: VendorItem; 
  onPriceChange: (itemId: number, newPrice: number) => void;
}) {
  const [newPrice, setNewPrice] = useState(item.customPrice);
  const Icon = itemIcons[item.item.name as keyof typeof itemIcons];

  return (
    <div className="flex flex-col sm:flex-row items-center gap-4 p-6 bg-white bg-opacity-80 rounded-lg shadow-lg border border-red-200 hover:border-red-300 transition-all duration-300 backdrop-blur-sm">
      <div className="flex items-center gap-4 w-full sm:w-auto">
        <div className="bg-red-100 p-3 rounded-full">
          <Icon className="h-8 w-8 text-red-600" />
        </div>
        <div>
          <h3 className="text-xl font-bold capitalize text-green-800">{item.item.name}</h3>
          <p className="text-sm text-gray-600">Base Price: €{item.item.basePrice}</p>
          <p className="text-lg font-semibold text-red-700 mt-1 mb-2">
            Current Price: €{item.customPrice} | Quantity: {item.stock}
          </p>
        </div>
      </div>
      <div className="flex flex-col sm:flex-row items-center gap-2 mt-4 sm:mt-0 sm:ml-auto">
        <div className="flex items-center gap-2">
          <Input
            type="number"
            min="0"
            step="0.01"
            value={newPrice}
            onChange={(e) => setNewPrice(Math.max(0, parseFloat(e.target.value) || 0))}
            className="w-24 border-red-200 focus:border-red-400 focus:ring-red-400"
          />
          <Button 
            onClick={() => onPriceChange(item.id, newPrice)}
            variant="secondary"
            className="bg-green-600 hover:bg-green-700 text-white transition-colors duration-300"
          >
            Update Price
          </Button>
        </div>
      </div>
    </div>
  );
}

export function VendorInventory() {
  const [inventory, setInventory] = useState<VendorItem[]>([]);
  const [error, setError] = useState<string>('');

  const fetchInventory = async () => {
    try {
      const response = await axios.get<VendorItem[]>('https://christmasmarket.onrender.com/vendor-items/me', {
        withCredentials: true
      });
      setInventory(response.data);
      setError('');
    } catch (err) {
      console.error('Failed to fetch inventory:', err);
      setError('Failed to load inventory. Please try again later.');
    }
  };

  const handlePriceChange = async (vendorItemId: number, newPrice: number) => {
    try {
      await axios.post(`https://christmasmarket.onrender.com/vendor-items/${vendorItemId}/set-price`, null, {
        params: { price: newPrice },
        withCredentials: true
      });
      await fetchInventory(); 
      setError('');
    } catch (err) {
      console.error('Failed to change price:', err);
      setError('Failed to update price. Please try again.');
    }
  };

  useEffect(() => {
    fetchInventory();
  }, []);

  return (
    <div className="min-h-screen bg-gradient-to-b from-red-900 to-green-900 relative overflow-hidden">
      <Sidebar />
      <div className="py-12 px-4 sm:px-6 lg:px-8 relative mt-16">
        <div className="max-w-3xl mx-auto relative">
          <h1 className="text-4xl font-bold text-center mb-8 text-yellow-300 shadow-text">
            Your Vendor Inventory
          </h1>
          
          {error && (
            <div className="mb-6 p-4 bg-red-500 text-white rounded-lg">
              {error}
            </div>
          )}

          <div className="space-y-6">
            {inventory.map((item) => (
              <InventoryItem
                key={item.id}
                item={item}
                onPriceChange={handlePriceChange}
              />
            ))}
            {inventory.length === 0 && !error && (
              <p className="text-center text-white bg-red-800 bg-opacity-80 p-6 rounded-lg shadow-lg border border-red-300">
                Your inventory is empty! Visit the storage to stock up on items.
              </p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default VendorInventory; 