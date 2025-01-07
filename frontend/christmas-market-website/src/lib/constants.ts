export const ITEM_NAMES = {
  'GLUHWEIN': 'Gluhwein',
  'APPLE_PIE': 'Classic Apple Pie',
  'SANTA_TOY': 'Santa Action Figure'
} as const;

export type Item = keyof typeof ITEM_NAMES; 