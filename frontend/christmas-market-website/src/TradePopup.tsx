import { useState } from 'react'
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Input } from "@/components/ui/input"
import { cn } from "@/lib/utils"
import { ITEM_NAMES, Item } from '@/lib/constants'

interface TradePopupProps {
  isOpen: boolean
  onClose: () => void
  onAccept: (item: Item, quantity: number, money: number) => void
}

export function TradePopup({ isOpen, onClose, onAccept }: TradePopupProps) {
  const [selectedItem, setSelectedItem] = useState<Item | null>(null)
  const [quantity, setQuantity] = useState<number>(0)
  const [money, setMoney] = useState(0)

  const handleItemChange = (value: Item) => {
    setSelectedItem(value)
    setQuantity(0)
  }

  const handleRequest = () => {
    if (selectedItem) {
      onAccept(selectedItem, quantity, money)
      onClose()
    }
  }

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className={cn(
        "sm:max-w-[425px]",
        "data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 data-[state=closed]:slide-out-to-left-1/2 data-[state=closed]:slide-out-to-top-[48%] data-[state=open]:slide-in-from-left-1/2 data-[state=open]:slide-in-from-top-[48%]",
        "fixed left-[50%] top-[50%] z-50 grid w-full max-w-lg translate-x-[-50%] translate-y-[-50%] gap-4 border bg-background p-6 shadow-lg duration-200 sm:rounded-lg md:w-full"
      )}>
        <DialogHeader>
          <DialogTitle>Trade Items</DialogTitle>
          <DialogDescription>
            Choose one item to trade, set its quantity, and add money if desired.
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <RadioGroup onValueChange={(value) => handleItemChange(value as Item)} className="grid grid-cols-3 gap-4">
            {Object.entries(ITEM_NAMES).map(([value, label]) => (
              <div key={value}>
                <RadioGroupItem value={value} id={value} className="peer sr-only" />
                <Label
                  htmlFor={value}
                  className="flex flex-col items-center justify-between rounded-md border-2 border-muted bg-popover p-4 hover:bg-accent hover:text-accent-foreground peer-data-[state=checked]:border-primary [&:has([data-state=checked])]:border-primary text-center"
                >
                  <span>{label}</span>
                </Label>
              </div>
            ))}
          </RadioGroup>
          
          {selectedItem && (
            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="quantity" className="text-right">
                Quantity
              </Label>
              <Select onValueChange={(value) => setQuantity(parseInt(value))}>
                <SelectTrigger className="col-span-3">
                  <SelectValue placeholder="Select quantity" />
                </SelectTrigger>
                <SelectContent>
                  {[1, 2, 3, 4, 5].map((num) => (
                    <SelectItem key={num} value={num.toString()}>
                      {num}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          )}
          
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="money" className="text-right">
              Money
            </Label>
            <Input
              id="money"
              type="number"
              className="col-span-3"
              value={money}
              onChange={(e) => setMoney(parseFloat(e.target.value) || 0)}
            />
          </div>
        </div>
        <Button onClick={handleRequest} disabled={!selectedItem || quantity === 0}>Request Trade</Button>
      </DialogContent>
    </Dialog>
  )
}


