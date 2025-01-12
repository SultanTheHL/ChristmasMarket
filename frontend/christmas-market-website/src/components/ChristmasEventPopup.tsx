import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";

interface ChristmasEventPopupProps {
  title: string;
  description: string;
  imageUrl: string;
  isOpen: boolean;
  onClose: () => void;
}

export function ChristmasEventPopup({ 
  title, 
  description, 
  imageUrl, 
  isOpen, 
  onClose 
}: ChristmasEventPopupProps) {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle className="text-2xl font-bold text-red-600">
            {title}
          </DialogTitle>
        </DialogHeader>
        <div className="relative w-full h-48 mb-4">
          <img
            src={imageUrl}
            alt={title}
            className="w-full h-full object-cover rounded-md"
          />
        </div>
        <p className="text-green-800 bg-green-100 p-4 rounded-md">
          {description}
        </p>
      </DialogContent>
    </Dialog>
  );
} 