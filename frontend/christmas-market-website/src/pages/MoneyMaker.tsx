import { useState } from 'react';
import { motion } from 'framer-motion';
import { DollarSign, Gift, TreePine as ChristmasTree } from 'lucide-react';
import axios from 'axios';
import Sidebar from '../Sidebar';

const MoneyFall = () => {
  const moneyItems = Array.from({ length: 50 }, (_, i) => i);

  return (
    <div className="fixed inset-0 pointer-events-none">
      {moneyItems.map((item) => (
        <motion.div
          key={item}
          initial={{ y: -100, x: Math.random() * window.innerWidth }}
          animate={{ y: window.innerHeight + 100 }}
          transition={{
            duration: Math.random() * 2 + 1,
            repeat: Infinity,
            repeatType: 'loop',
            ease: 'linear',
            delay: Math.random() * 2,
          }}
          className="absolute text-green-500"
        >
          <DollarSign className="w-8 h-8" />
        </motion.div>
      ))}
    </div>
  );
};

export function MoneyMaker() {
  const [isAnimating, setIsAnimating] = useState(false);

  const handleMakeMoney = async () => {
    try {
      await axios.post('https://christmasmarket.onrender.com/customers/add-money', null, {
        params: { amount: 10 },
        withCredentials: true
      });
      
      setIsAnimating(true);
      setTimeout(() => setIsAnimating(false), 3000);
      
      window.dispatchEvent(new Event('wallet-update'));
      
    } catch (err) {
      console.error('Failed to make money:', err);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-red-700 to-green-800">
      <Sidebar />
      <div className="flex flex-col items-center justify-center p-4 pt-24">
        <motion.div
          initial={{ scale: 0 }}
          animate={{ scale: 1 }}
          transition={{ type: 'spring', stiffness: 260, damping: 20 }}
          className="text-6xl mb-8 text-yellow-300"
        >
          <ChristmasTree className="w-24 h-24 inline-block mr-4" />
          <Gift className="w-24 h-24 inline-block" />
        </motion.div>
        
        <h1 className="text-4xl md:text-6xl font-bold text-white mb-8 text-center">
          Christmas Money Maker
        </h1>
        
        <motion.button
          whileHover={{ scale: 1.1 }}
          whileTap={{ scale: 0.9 }}
          onClick={handleMakeMoney}
          className="bg-yellow-400 hover:bg-yellow-300 text-red-800 font-bold py-4 px-8 rounded-full text-2xl shadow-lg"
        >
          Make Money
        </motion.button>

        {isAnimating && <MoneyFall />}

        <div className="absolute bottom-0 left-0 right-0">
          <div className="h-20 bg-white rounded-t-full" />
        </div>
      </div>
    </div>
  );
}

export default MoneyMaker; 