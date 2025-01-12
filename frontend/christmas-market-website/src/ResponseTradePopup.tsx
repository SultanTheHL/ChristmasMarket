import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { cn } from "@/lib/utils"
import { Item } from '@/lib/types';


interface ResponseTradePopupProps {
  isOpen: boolean
  onClose: () => void
  onAccept: () => void
  onDecline: () => void
  item: Item
  quantity: number
  money: number
  buyerEmail: string
}

export function ResponseTradePopup({ isOpen, onClose, onAccept, onDecline, item, quantity, money, buyerEmail }: ResponseTradePopupProps) {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className={cn(
        "sm:max-w-[425px]",
        "data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 data-[state=closed]:slide-out-to-left-1/2 data-[state=closed]:slide-out-to-top-[48%] data-[state=open]:slide-in-from-left-1/2 data-[state=open]:slide-in-from-top-[48%]",
        "fixed left-[50%] top-[50%] z-50 grid w-full max-w-lg translate-x-[-50%] translate-y-[-50%] gap-4 border bg-background p-6 shadow-lg duration-200 sm:rounded-lg md:w-full"
      )}>
        <DialogHeader>
          <DialogTitle>Trade Request</DialogTitle>
          <DialogDescription>
            You have received a trade request from {buyerEmail}. Would you like to accept?
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-2 gap-4">
            <div className="font-semibold">From:</div>
            <div>{buyerEmail}</div>
            <div className="font-semibold">Item:</div>
            <div>{item.name}</div>
            <div className="font-semibold">Quantity:</div>
            <div>{quantity}</div>
            <div className="font-semibold">Money Offered:</div>
            <div>${money.toFixed(2)}</div>
          </div>
        </div>
        <div className="flex justify-end space-x-2">
          <Button onClick={onDecline} variant="outline">Decline</Button>
          <Button onClick={onAccept}>Accept</Button>
        </div>
      </DialogContent>
    </Dialog>
  )
}


