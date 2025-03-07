import { useState } from "react";
import { motion } from "framer-motion";

const FloatingLabelInput = ({ type, label, value, onChange }) => {
    const [isFocused, setIsFocused] = useState(false);

    return (
        <div className="relative w-full mb-4">
            <motion.label
                className="absolute top-1 left-3 text-gray-900 transition-all text-sm pointer-events-none"
                animate={isFocused || value ? { y: -6, x: -7, scale: 0.70, color: "#3b82f6" } : { y: "50%", x: 10, scale: 1, color: "#6b7280" }}
                initial={{ y: "50%", x: 10, scale: 1 }}
                transition={{ duration: 0.1, ease: "easeInOut" }}
                style={{ transform: "translateY(-50%)" }} 
            >
                {label}
            </motion.label>

            <input
                type={type}
                value={value}
                onChange={onChange}
                onFocus={() => setIsFocused(true)}
                onBlur={() => setIsFocused(false)}
                className="w-full p-3 border-1 border-gray-500 rounded-2xl focus:outline-none focus:border-blue-500 shadow-sm"
                required
            />
        </div>
    );
};


export default FloatingLabelInput;