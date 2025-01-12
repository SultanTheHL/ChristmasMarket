import { useState } from 'react';
import { ShoppingCart, Plus, Minus } from 'lucide-react';
import { Button } from "@/components/ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import axios from 'axios';

interface CartItem {
  id: number;
  vendorItem: {
    id: number;
    item: {
      name: string;
    };
    customPrice: number;
  };
  quantity: number;
}

export function CartPopup() {
  const [isOpen, setIsOpen] = useState(false);
  const [cartItems, setCartItems] = useState<CartItem[]>([]);

  const fetchCart = async () => {
    try {
      const response = await axios.get<CartItem[]>('https://christmasmarket.onrender.com/cart/items', {
        withCredentials: true
      });
      setCartItems(response.data || []);
    } catch (err) {
      window.alert('Failed to fetch cart:');
    }
  };

  const updateQuantity = async (itemId: number, quantity: number, increment: boolean) => {
    try {
      if (quantity === 0) {
        await axios.post(`https://christmasmarket.onrender.com/cart/remove/${itemId}`, null, {
          withCredentials: true
        });
      } else if(increment) {
        await axios.post(`https://christmasmarket.onrender.com/cart/add/${itemId}`, null, {
          params: { quantity: 1 },
          withCredentials: true
        });
      } else {
        await axios.post(`https://christmasmarket.onrender.com/cart/add/${itemId}`, null, {
          params: { quantity: -1 },
          withCredentials: true
        });
      }
      await fetchCart();
    } catch (err) {
      console.error('Failed to update cart:', err);
    }
  };

  const handleCheckout = async () => {
    try {
      const response = await axios.post('https://christmasmarket.onrender.com/cart/checkout', null, {
        withCredentials: true
      });
      alert(response.data);
      setIsOpen(false);
      await fetchCart();
      window.dispatchEvent(new Event('cart-checkout'));
    } catch (err) {
      window.alert('Failed to checkout:' + err);
    }
  };

  const total = cartItems.reduce((sum, item) => 
    sum + item.vendorItem.customPrice * item.quantity, 0
  );

  return (
    <Popover open={isOpen} onOpenChange={(open) => {
      setIsOpen(open);
      if (open) fetchCart();
    }}>
      <PopoverTrigger asChild>
        <Button variant="outline" size="icon" className="relative">
          <ShoppingCart className="h-[1.2rem] w-[1.2rem]" />
          {cartItems.length > 0 && (
            <span className="absolute -top-1 -right-1 h-4 w-4 rounded-full bg-red-500 text-white text-xs flex items-center justify-center">
              {cartItems.reduce((sum, item) => sum + item.quantity, 0)}
            </span>
          )}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-80" align="end">
        <div className="grid gap-4">
          <div className="flex justify-between items-center">
            <h3 className="font-semibold">Shopping Cart</h3>
            <span className="text-sm text-gray-500">
              {cartItems.length} items
            </span>
          </div>
          <div className="max-h-[300px] overflow-auto">
            {cartItems.map((item) => (
              <div key={item.id} className="flex items-center justify-between py-2">
                <div>
                  <p className="font-medium">{item.vendorItem.item.name}</p>
                  <p className="text-sm text-gray-500">
                    €{item.vendorItem.customPrice.toFixed(2)} each
                  </p>
                </div>
                <div className="flex items-center gap-2">
                  <Button
                    variant="outline"
                    size="icon"
                    className="h-8 w-8"
                    onClick={() => updateQuantity(item.vendorItem.id, item.quantity - 1, false)}
                  >
                    <Minus className="h-4 w-4" />
                  </Button>
                  <span className="w-8 text-center">{item.quantity}</span>
                  <Button
                    variant="outline"
                    size="icon"
                    className="h-8 w-8"
                    onClick={() => updateQuantity(item.vendorItem.id, item.quantity + 1, true)}
                  >
                    <Plus className="h-4 w-4" />
                  </Button>
                </div>
              </div>
            ))}
          </div>
          {cartItems.length > 0 ? (
            <div className="border-t pt-4">
              <div className="flex justify-between mb-4">
                <span className="font-semibold">Total:</span>
                <span className="font-bold">€{total.toFixed(2)}</span>
              </div>
              <Button 
                className="w-full" 
                onClick={handleCheckout}
              >
                Checkout
              </Button>
            </div>
          ) : (
            <p className="text-center text-gray-500">Your cart is empty</p>
          )}
        </div>
      </PopoverContent>
    </Popover>
  );
} 