import { useState } from 'react';
import { motion } from 'framer-motion';
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { DiamondIcon } from 'lucide-react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const Login: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const params = new URLSearchParams();
            params.append('email', email);
            params.append('password', password);
            
            await axios.post('http://localhost:8080/auth/login', params, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                withCredentials: true,
            });
            
            const profileResponse = await axios.get('http://localhost:8080/auth/isCustomer', {
                withCredentials: true,
            });
            
            if (profileResponse.data) {
                window.location.href = '/dashboard';
            } else {
                window.location.href = '/vendor/dashboard';
            }
        } catch (err: any) {
            setError(err.response?.data || 'Login failed.');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-red-900 to-green-900 bg-cover bg-center bg-no-repeat">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.8 }}
                className="w-full max-w-md p-8 rounded-2xl backdrop-blur-sm bg-black/30 border border-red-800 shadow-2xl"
            >
                <div className="flex justify-center mb-8">
                    <DiamondIcon className="w-12 h-12 text-yellow-400" />
                </div>
                <h1 className="text-3xl font-extrabold text-center text-white mb-8">
                    Christmas Market Login
                </h1>
                {error && (
                    <div className="mb-4 p-3 bg-red-500/50 border border-red-600 rounded text-white text-center">
                        {error}
                    </div>
                )}
                <form onSubmit={handleLogin} className="space-y-6">
                    <div className="space-y-2">
                        <Label htmlFor="email" className="text-sm font-medium text-gray-200">
                            Email
                        </Label>
                        <Input
                            id="email"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            placeholder="your@email.com"
                            className="bg-gray-800/50 border-gray-700 text-white placeholder-gray-400 focus:ring-red-400 focus:border-red-400 transition-all duration-300"
                        />
                    </div>
                    <div className="space-y-2">
                        <Label htmlFor="password" className="text-sm font-medium text-gray-200">
                            Password
                        </Label>
                        <Input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            placeholder="••••••••"
                            className="bg-gray-800/50 border-gray-700 text-white placeholder-gray-400 focus:ring-red-400 focus:border-red-400 transition-all duration-300"
                        />
                    </div>
                    <Button
                        type="submit"
                        className="w-full bg-gradient-to-r from-red-500 to-red-700 hover:from-red-600 hover:to-red-800 text-white font-bold py-3 rounded-lg transition-all duration-300 transform hover:scale-105"
                    >
                        Sign In
                    </Button>
                </form>
                <p className="mt-6 text-center text-sm text-gray-400">
                    Don't have an account?{' '}
                    <Link to="/register" className="text-red-400 hover:text-red-300 font-semibold">
                        Register here
                    </Link>
                </p>
            </motion.div>
        </div>
    );
};

export default Login;
