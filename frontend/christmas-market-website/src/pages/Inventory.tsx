import { useState, useEffect } from 'react';
import { Wine, PieChart, Gift } from 'lucide-react';
import axios from 'axios';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import Sidebar from '../Sidebar';

interface CustomerItem {
  id: number;
  customer: {
    id: number;
    email: string;
    wallet: number;
  };
  item: {
    id: number;
    name: string;
    item_type: string;
    basePrice: number;
    storable: boolean;
  };
  quantity: number;
}

const itemIcons = {
  'Gluhwein': Wine,
  'Classic Apple Pie': PieChart,
  'Santa Action Figure': Gift,
} as const;


function InventoryItem({ item, onThrowOut }: { 
  item: CustomerItem; 
  onThrowOut: (itemId: number, quantity: number) => void;
}) {
  const [throwOutAmount, setThrowOutAmount] = useState(1);
  const Icon = itemIcons[item.item.name as keyof typeof itemIcons];

  return (
    <div className="flex flex-col sm:flex-row items-center gap-4 p-6 bg-white bg-opacity-80 rounded-lg shadow-lg border border-red-200 hover:border-red-300 transition-all duration-300 backdrop-blur-sm">
      <div className="flex items-center gap-4 w-full sm:w-auto">
        <div className="bg-red-100 p-3 rounded-full">
          <Icon className="h-8 w-8 text-red-600" />
        </div>
        <div>
          <h3 className="text-xl font-bold capitalize text-green-800">{item.item.name}</h3>
          <p className="text-sm text-gray-600">
            {item.item.storable ? "Can be stored" : "Cannot be stored"}
          </p>
          <p className="text-lg font-semibold text-red-700 mt-1">Quantity: {item.quantity}</p>
        </div>
      </div>
      <div className="flex flex-col sm:flex-row items-center gap-2 mt-4 sm:mt-0 sm:ml-auto">
        <div className="flex items-center gap-2">
          <Input
            type="number"
            min={1}
            max={item.quantity}
            value={throwOutAmount}
            onChange={(e) => setThrowOutAmount(Math.min(item.quantity, Math.max(1, parseInt(e.target.value) || 1)))}
            className="w-16 border-red-200 focus:border-red-400 focus:ring-red-400"
          />
          <Button 
            onClick={() => onThrowOut(item.id, throwOutAmount)} 
            variant="destructive" 
            size="sm" 
            className="bg-red-600 hover:bg-red-700 transition-colors duration-300"
          >
            Throw Out
          </Button>
        </div>
      </div>
    </div>
  );
}

export function Inventory() {
  const [inventory, setInventory] = useState<CustomerItem[]>([]);

  const fetchInventory = async () => {
    try {
      const response = await axios.get('http://localhost:8080/customer-item/me', {
        withCredentials: true
      });
      setInventory(response.data);
    } catch (err) {
      console.error('Failed to fetch inventory:', err);
    }
  };

  const handleThrowOut = async (itemId: number, quantity: number) => {
    try {
      await axios.delete(`http://localhost:8080/customer-item/throw/${itemId}`, {
        params: { quantity },
        withCredentials: true
      });
      fetchInventory();
    } catch (err) {
      console.error('Failed to throw out item:', err);
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
            Your Festive Inventory
          </h1>
          <div className="space-y-6">
            {inventory.map((item) => (
              <InventoryItem
                key={item.id}
                item={item}
                onThrowOut={(_, quantity) => handleThrowOut(item.item.id, quantity)}
              />
            ))}
            {inventory.length === 0 && (
              <p className="text-center text-white bg-red-800 bg-opacity-80 p-6 rounded-lg shadow-lg border border-red-300">
                Your inventory is as empty as Santa's workshop after Christmas! Time to restock!
              </p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Inventory; 