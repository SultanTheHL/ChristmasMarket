export interface Item {
  id: number;
  name: string;
  basePrice: number;
  storable: boolean;
  item_type: 'GLUHWEIN' | 'APPLE_PIE' | 'SANTA_TOY';
} 