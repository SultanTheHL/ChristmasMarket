import { motion } from 'framer-motion';
import { Package, Box, Wallet, PieChart } from 'lucide-react';
import { Link } from 'react-router-dom';
import Sidebar from '../Sidebar';

const DashboardCard = ({ title, icon: Icon, to, description }: {
  title: string;
  icon: any;
  to: string;
  description: string;
}) => (
  <Link to={to}>
    <motion.div
      whileHover={{ scale: 1.05 }}
      whileTap={{ scale: 0.95 }}
      className="bg-white/10 backdrop-blur-sm p-6 rounded-xl border border-red-800/30 shadow-xl hover:border-red-400/50 transition-all duration-300"
    >
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-xl font-bold text-white">{title}</h3>
        <Icon className="w-8 h-8 text-yellow-400" />
      </div>
      <p className="text-gray-300">{description}</p>
    </motion.div>
  </Link>
);

export function VendorDashboard() {
  const dashboardItems = [
    {
      title: "Buy From Storage",
      icon: Package,
      to: "/vendor/storage",
      description: "Purchase items from the central storage"
    },
    {
      title: "Your Stall",
      icon: Box,
      to: "/vendor/inventory",
      description: "Manage your items and prices"
    },
    {
      title: "Take Loan",
      icon: Wallet,
      to: "/vendor/loan",
      description: "Get financial assistance for your business"
    },
    {
      title: "Statistics",
      icon: PieChart,
      to: "/vendor/stats",
      description: "View your business performance"
    }
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-red-900 to-green-900">
      <Sidebar />
      <div className="container mx-auto px-4 py-8 pt-24">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8 }}
        >
          <h1 className="text-4xl font-bold text-center text-white mb-12">
            Vendor Dashboard
          </h1>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-4xl mx-auto">
            {dashboardItems.map((item) => (
              <DashboardCard key={item.to} {...item} />
            ))}
          </div>
        </motion.div>
      </div>
    </div>
  );
} 