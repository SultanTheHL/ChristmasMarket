import { useState, useEffect } from 'react';
import axios from 'axios';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Wine, PieChart, Gift } from 'lucide-react';
import Sidebar from '../Sidebar';

interface VendorItem {
  id: number;
  item: {
    id: number;
    name: string;
    basePrice: number;
  };
  customPrice: number;
  quantity: number;
  soldCount: number;
}

const itemIcons = {
  'Gluhwein': Wine,
  'Classic Apple Pie': PieChart,
  'Santa Action Figure': Gift,
} as const;

function ProductCard({ name, sold, profit, icon: Icon }: {
  name: string;
  sold: number;
  profit: number;
  icon: any;
}) {
  return (
    <Card className="bg-white/90 shadow-lg hover:shadow-xl transition-shadow duration-300">
      <CardHeader>
        <CardTitle className="text-xl font-semibold text-center flex items-center justify-center gap-2">
          <Icon className="h-6 w-6 text-red-600" />
          {name}
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div className="text-center">
          <p className="text-3xl font-bold text-green-600">{sold}</p>
          <p className="text-sm text-gray-500">Units Sold</p>
        </div>
        <div className="text-center mt-4">
          <p className="text-3xl font-bold text-blue-600">€{profit}</p>
          <p className="text-sm text-gray-500">Estimated Profit</p>
        </div>
      </CardContent>
    </Card>
  );
}

function TotalProfit({ total }: { total: number }) {
  return (
    <Card className="bg-red-700 text-white shadow-lg">
      <CardHeader>
        <CardTitle className="text-2xl font-semibold text-center">Total Estimated Profit</CardTitle>
      </CardHeader>
      <CardContent>
        <p className="text-4xl font-bold text-center">€{total}</p>
      </CardContent>
    </Card>
  );
}

export function Stats() {
  const [vendorItems, setVendorItems] = useState<VendorItem[]>([]);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    const fetchVendorItems = async () => {
      try {
        const response = await axios.get('http://localhost:8080/vendor-items/me', {
          withCredentials: true
        });
        setVendorItems(response.data);
      } catch (err) {
        console.error('Failed to fetch vendor items:', err);
        setError('Failed to load statistics. Please try again later.');
      }
    };

    fetchVendorItems();
  }, []);

  // Mock profit calculation (20% profit margin)
  const calculateMockProfit = (item: VendorItem) => {
    const revenue = item.soldCount * item.customPrice;
    const cost = item.soldCount * item.item.basePrice;
    return Math.round((revenue - cost) * 100) / 100;
  };

  const products = vendorItems.map(item => ({
    name: item.item.name,
    sold: item.soldCount,
    profit: calculateMockProfit(item),
    icon: itemIcons[item.item.name as keyof typeof itemIcons]
  }));

  const totalProfit = products.reduce((sum, product) => sum + product.profit, 0);

  return (
    <div className="min-h-screen bg-gradient-to-br from-red-900 to-green-900">
      <Sidebar />
      <div className="container mx-auto px-4 py-8 pt-24">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-4xl font-bold text-center text-yellow-300 mb-12">
            Your Sales Statistics
          </h1>

          {error && (
            <div className="mb-6 p-4 bg-red-500 text-white rounded-lg">
              {error}
            </div>
          )}

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            {products.map((product) => (
              <ProductCard 
                key={product.name} 
                name={product.name}
                sold={product.sold}
                profit={product.profit}
                icon={product.icon}
              />
            ))}
          </div>

          <TotalProfit total={Math.round(totalProfit * 100) / 100} />

          {products.length === 0 && !error && (
            <p className="text-center text-white bg-red-800 bg-opacity-80 p-6 rounded-lg shadow-lg border border-red-300">
              No sales data available yet. Start selling to see your statistics!
            </p>
          )}
        </div>
      </div>
    </div>
  );
}

export default Stats; 