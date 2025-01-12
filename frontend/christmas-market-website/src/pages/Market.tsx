import { useState, useEffect } from 'react';
import axios from 'axios';
import { Wine, PieChart, Gift, ChevronLeft, ChevronRight } from 'lucide-react';
import Sidebar from '../Sidebar';
import { ChristmasEventPopup } from '@/components/ChristmasEventPopup';
import gluhweinGift from '../assets/gluhweingift.webp';
import moneyGift from '../assets/moneygift.webp';
import everythingDiscount from '../assets/everythingdiscount.webp';

interface Vendor {
  id: number;
  name: string;
  email: string;
}

interface VendorItem {
  id: number;
  vendor: Vendor;
  stock: number;
  soldCount: number;
  customPrice: number;
  item: {
    id: number;
    name: string;
    item_type: string;
  };
}

const itemIcons = {
  'Gluhwein': Wine,
  'Classic Apple Pie': PieChart,
  'Santa Action Figure': Gift,
} as const;

const SANTA_EVENTS = {
  'Gift Everyone Gluhwein': {
    title: "Santa's Glühwein Gift!",
    description: "Ho Ho Ho! Santa has decided to spread holiday cheer by gifting everyone some warm Glühwein!",
    imageUrl: gluhweinGift
  },
  'Bonus Money': {
    title: "Christmas Bonus!",
    description: "Santa's feeling generous! Everyone gets some bonus Christmas money!",
    imageUrl: moneyGift
  },
  'Discount Event': {
    title: "Market-Wide Discount!",
    description: "Santa has worked his magic! All items in the market are now discounted!",
    imageUrl: everythingDiscount
  }
} as const;

export function Market() {
  const [vendors, setVendors] = useState<Vendor[]>([]);
  const [vendorItems, setVendorItems] = useState<Record<number, VendorItem[]>>({});
  const [cartQuantities, setCartQuantities] = useState<Record<number, number>>({});
  const [currentPage, setCurrentPage] = useState(1);
  const vendorsPerPage = 4;
  const [eventPopup, setEventPopup] = useState<{
    title: string;
    description: string;
    imageUrl: string;
  } | null>(null);

  useEffect(() => {
    fetchVendors();
  }, []);

  const fetchVendors = async () => {
    try {
      const response = await axios.get('http://localhost:8080/vendors', {
        withCredentials: true
      });
      setVendors(response.data);
      
      response.data.forEach((vendor: Vendor) => {
        fetchVendorItems(vendor.id);
      });
    } catch (err) {
      console.error('Failed to fetch vendors:', err);
    }
  };

  const fetchVendorItems = async (vendorId: number) => {
    try {
      const response = await axios.get(`http://localhost:8080/vendor-items/vendor/${vendorId}`, {
        withCredentials: true
      });
      setVendorItems(prev => ({
        ...prev,
        [vendorId]: response.data
      }));
    } catch (err) {
      console.error(`Failed to fetch items for vendor ${vendorId}:`, err);
    }
  };

  const triggerRandomEvent = async () => {
    if (Math.random() < 0.9) {
      const events = Object.keys(SANTA_EVENTS);
      const randomEvent = events[Math.floor(Math.random() * events.length)];
      const randomAmount = Math.floor(Math.random() * 10) + 1; 

      try {
        const response = await axios.post('http://localhost:8080/santa/trigger-event', null, {
          params: {
            eventName: randomEvent,
            amount: randomAmount
          },
          withCredentials: true
        });
        
        setEventPopup(SANTA_EVENTS[randomEvent as keyof typeof SANTA_EVENTS]);
        
        fetchVendors();
      } catch (err) {
        console.error('Failed to trigger Santa event:', err);
      }
    }
  };

  const handleAddToCart = async (vendorItemId: number) => {
    try {
      const quantity = cartQuantities[vendorItemId] || 1;
      await axios.post(`http://localhost:8080/cart/add/${vendorItemId}`, null, {
        params: { quantity },
        withCredentials: true
      });
      alert('Item added to cart!');
      vendors.forEach(vendor => fetchVendorItems(vendor.id));
    } catch (err) {
      console.error('Failed to add item to cart:', err);
    }
  };

  useEffect(() => {
    const handleCheckout = async () => {
      await triggerRandomEvent();
      vendors.forEach(vendor => fetchVendorItems(vendor.id));
    };

    window.addEventListener('cart-checkout', handleCheckout);
    return () => window.removeEventListener('cart-checkout', handleCheckout);
  }, [vendors]);

  const indexOfLastVendor = currentPage * vendorsPerPage;
  const indexOfFirstVendor = indexOfLastVendor - vendorsPerPage;
  const currentVendors = vendors.slice(indexOfFirstVendor, indexOfLastVendor);
  const totalPages = Math.ceil(vendors.length / vendorsPerPage);

  const nextPage = () => {
    setCurrentPage(prev => Math.min(prev + 1, totalPages));
  };

  const prevPage = () => {
    setCurrentPage(prev => Math.max(prev - 1, 1));
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <Sidebar />
      <main className="container mx-auto px-4 py-8 mt-16">
        <h1 className="text-3xl font-bold mb-8">Christmas Market Vendors</h1>
        <div className="grid gap-8">
          {currentVendors.map(vendor => (
            <div key={vendor.id} className="bg-white shadow-lg rounded-lg overflow-hidden">
              <div className="bg-red-600 text-white p-4">
                <h2 className="text-xl font-semibold">{vendor.name}</h2>
              </div>
              <div className="p-4">
                {vendorItems[vendor.id]?.filter(item => item.stock > 0).map(item => {
                  const Icon = itemIcons[item.item.name as keyof typeof itemIcons];
                  return Icon ? (
                    <div key={item.id} className="flex items-center justify-between mb-4 p-2 border-b">
                      <div className="flex items-center gap-3">
                        <Icon className="h-6 w-6" />
                        <div>
                          <p className="font-medium">{item.item.name}</p>
                          <p className="text-sm text-gray-500">Stock: {item.stock}</p>
                        </div>
                      </div>
                      <div className="flex items-center gap-4">
                        <span className="font-bold">€{item.customPrice.toFixed(2)}</span>
                        <input
                          type="number"
                          min="1"
                          max={item.stock}
                          value={cartQuantities[item.id] || 1}
                          onChange={(e) => setCartQuantities(prev => ({
                            ...prev,
                            [item.id]: parseInt(e.target.value)
                          }))}
                          className="w-16 p-1 border rounded"
                        />
                        <button
                          onClick={() => handleAddToCart(item.id)}
                          className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                          disabled={item.stock === 0}
                        >
                          Add to Cart
                        </button>
                      </div>
                    </div>
                  ) : null;
                })}
                {!vendorItems[vendor.id]?.filter(item => item.stock > 0).length && (
                  <p className="text-gray-500 text-center py-4">No items available</p>
                )}
              </div>
            </div>
          ))}
        </div>
        
        {totalPages > 1 && (
          <div className="flex justify-center items-center gap-4 mt-8">
            <button
              onClick={prevPage}
              disabled={currentPage === 1}
              className="p-2 rounded-full bg-red-600 text-white disabled:bg-gray-300 disabled:cursor-not-allowed"
            >
              <ChevronLeft className="h-6 w-6" />
            </button>
            <span className="text-lg font-medium">
              Page {currentPage} of {totalPages}
            </span>
            <button
              onClick={nextPage}
              disabled={currentPage === totalPages}
              className="p-2 rounded-full bg-red-600 text-white disabled:bg-gray-300 disabled:cursor-not-allowed"
            >
              <ChevronRight className="h-6 w-6" />
            </button>
          </div>
        )}
      </main>
      
      {eventPopup && (
        <ChristmasEventPopup
          title={eventPopup.title}
          description={eventPopup.description}
          imageUrl={eventPopup.imageUrl}
          isOpen={!!eventPopup}
          onClose={() => setEventPopup(null)}
        />
      )}
    </div>
  );
}

export default Market;