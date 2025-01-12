import { useState, useEffect } from 'react';
import axios from 'axios';
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import Sidebar from '../Sidebar';
import { Item } from '@/lib/types';


export function BuyFromStorage() {
  const [items, setItems] = useState<Item[]>([]);
  const [quantities, setQuantities] = useState<{ [key: number]: number }>({});

  const fetchItems = async () => {
    try {
      const response = await axios.get<Item[]>('https://christmasmarket.onrender.com/items', {
        withCredentials: true
      });
      setItems(response.data);
    } catch (err) {
      console.error('Failed to fetch items:', err);
    }
  };

  const handleBuy = async (itemId: number) => {
    try {
      const quantity = quantities[itemId] || 1;
      await axios.post(`https://christmasmarket.onrender.com/vendor-items/buy`, null, {
        params: { itemId, quantity },
        withCredentials: true
      });
      alert('Purchase successful!');
    } catch (err) {
      console.error('Failed to buy item:', err);
      alert('Failed to purchase item');
    }
  };

  useEffect(() => {
    fetchItems();
  }, []);

  return (
    <div className="min-h-screen bg-gradient-to-br from-red-900 to-green-900">
      <Sidebar />
      <div className="container mx-auto px-4 py-8 pt-24">
        <h1 className="text-4xl font-bold text-center text-white mb-12">
          Buy From Storage
        </h1>
        <div className="grid gap-6 max-w-4xl mx-auto">
          {items.map((item) => (
            <div key={item.id} className="bg-white/10 backdrop-blur-sm p-6 rounded-xl border border-red-800/30">
              <div className="flex justify-between items-center mb-4">
                <div>
                  <h3 className="text-xl font-bold text-white">{item.name}</h3>
                  <p className="text-gray-300">Base Price: €{item.basePrice}</p>
                </div>
                <div className="flex items-center gap-4">
                  <Input
                    type="number"
                    min="1"
                    value={quantities[item.id] || 1}
                    onChange={(e) => setQuantities({
                      ...quantities,
                      [item.id]: Math.max(1, parseInt(e.target.value) || 1)
                    })}
                    className="w-20"
                  />
                  <Button onClick={() => handleBuy(item.id)}>
                    Buy
                  </Button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
} 