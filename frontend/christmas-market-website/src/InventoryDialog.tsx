
import { useState, useEffect } from 'react'
import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog"
import { Wine, PieChart, Gift } from 'lucide-react'
import axios from 'axios'

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
  };
  quantity: number;
}

const itemIcons = {
  'Gluhwein': Wine,
  'Classical Apple Pie': PieChart,
  'Santa Action Figure': Gift,
} as const;

export function InventoryDialog({ customerEmail }: { customerEmail: string }) {
  const [open, setOpen] = useState(false);
  const [inventory, setInventory] = useState<CustomerItem[]>([]);

  const fetchInventory = async () => {
    try {
      const response = await axios.get('http://localhost:8080/customer-item', {
        params: { email: customerEmail },
        withCredentials: true
      });
      setInventory(response.data);
    } catch (err) {
      console.error('Failed to fetch inventory:', err);
    }
  };

  useEffect(() => {
    if (open) {
      fetchInventory();
    }
  }, [open]);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <Button variant="outline" onClick={() => setOpen(true)}>
        Inventory
      </Button>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Inventory</DialogTitle>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          {inventory.map((item) => {
            const Icon = itemIcons[item.item.name as keyof typeof itemIcons];
            return (
              <div key={item.id} className="flex items-center gap-4">
                <Icon className="h-6 w-6" />
                <span className="font-medium">{item.item.name}</span>
                <span className="ml-auto">{item.quantity}</span>
              </div>
            )
          })}
          {inventory.length === 0 && (
            <p className="text-center text-gray-500">Your inventory is empty.</p>
          )}
        </div>
      </DialogContent>
    </Dialog>
  )
} 