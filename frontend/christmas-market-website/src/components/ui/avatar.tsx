import * as React from "react";

export interface AvatarProps {
  src?: string;
  alt: string;
  fallback?: string;
}

export const Avatar: React.FC<AvatarProps> = ({ src, alt, fallback }) => {
  return (
    <div className="relative w-10 h-10 rounded-full overflow-hidden bg-gray-200">
      {src ? (
        <img src={src} alt={alt} className="w-full h-full object-cover" />
      ) : (
        <span className="absolute inset-0 flex items-center justify-center text-gray-500">
          {fallback || "?"}
        </span>
      )}
    </div>
  );
};
