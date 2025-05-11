import { useEffect, useState, useRef } from "react";
import { messageApi } from "../services/messageApi";
import { FiSend } from "react-icons/fi";

const MessagePage = () => {
  const [connectedUsers, setConnectedUsers] = useState([]);
  const [activeUser, setActiveUser] = useState(null);
  const [messageInput, setMessageInput] = useState("");
  const [messages, setMessages] = useState([]);
  const [hoveredMessageIndex, setHoveredMessageIndex] = useState(null); // Zmienna do przechowywania indeksu wiadomości najechanej
  const messagesEndRef = useRef(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  useEffect(() => {
    const fetchConnected = async () => {
      try {
        const res = await messageApi.getConnectedUsers();
        setConnectedUsers(res);
      } catch (error) {
        console.error("Error fetching connected users:", error);
      }
    };

    fetchConnected();
  }, []);

  const fetchMessages = async () => {
    if (activeUser) {
      const msgs = await messageApi.getMessagesWith(activeUser.userId);
      setMessages(msgs);
    }
  };

  useEffect(() => {
    const intervalId = setInterval(() => {
      fetchMessages();
    }, 5000);

    return () => clearInterval(intervalId);
  }, [activeUser]);

  return (
    <div className="pt-24 max-w-7xl mx-auto px-4 h-[calc(100vh-10rem)] mt-10">
      <div className="flex h-full bg-white shadow rounded-2xl overflow-hidden">
        <div className="w-80 h-full border-r border-gray-100 bg-gray-50 overflow-y-auto flex-shrink-0">
          <h2 className="text-md font-semibold text-gray-700 p-4 tracking-wide uppercase text-center">
            Conversations
          </h2>
          {connectedUsers.map((user) => (
            <div
              key={user.userId}
              onClick={async () => {
                setActiveUser(user);
                const msgs = await messageApi.getMessagesWith(user.userId);
                setMessages(msgs);
              }}
              className={`flex items-center gap-3 px-4 py-3 cursor-pointer transition-all rounded-xl mx-2 mb-1 ${
                activeUser?.userId === user.userId
                  ? "bg-blue-100"
                  : "hover:bg-gray-100"
              }`}
            >
              <img
                src={user.profileImage}
                alt={`${user.firstName} ${user.lastName}`}
                className="w-10 h-10 rounded-full object-cover"
              />
              <div className="flex flex-col min-w-0">
                <span className="font-medium text-gray-800 text-sm truncate">
                  {user.firstName} {user.lastName}
                </span>
              </div>
            </div>
          ))}
        </div>

        <div className="flex-1 flex flex-col min-w-0">
          {activeUser ? (
            <>
              <div className="flex-shrink-0 flex items-center gap-4 px-6 py-4 border-b border-gray-100 shadow-sm">
                <img
                  src={activeUser.profileImage}
                  alt={`${activeUser.firstName} ${activeUser.lastName}`}
                  className="w-10 h-10 rounded-full object-cover"
                />
                <div>
                  <div className="text-gray-900 font-semibold">
                    {activeUser.firstName} {activeUser.lastName}
                  </div>
                  <div className="text-xs text-gray-400">Active now</div>
                </div>
              </div>

              <div className="flex-1 min-h-0 overflow-y-auto px-6 py-4">
                <div className="space-y-2">
                  {messages.length === 0 ? (
                    <div className="text-gray-400 italic text-sm flex items-center justify-center">
                      No messages yet. Start the conversation!
                    </div>
                  ) : (
                    messages.map((msg, index) => (
                      <div
                        key={index}
                        className={`flex ${msg.own ? "justify-end" : "justify-start"}`}
                        onMouseEnter={() => setHoveredMessageIndex(index)} // Ustawienie indeksu wiadomości po najechaniu
                        onMouseLeave={() => setHoveredMessageIndex(null)} // Usunięcie indeksu po opuszczeniu
                      >
                        <div
                          className={`max-w-[80%] w-fit px-4 py-2 rounded-2xl shadow text-sm ${
                            msg.own
                              ? "bg-blue-600 text-white"
                              : "bg-gray-200 text-gray-800"
                          }`}
                        >
                          <span className="break-words whitespace-pre-wrap">
                            {msg.content}
                          </span>
                        </div>

                        {/* Dodanie godziny w osobnym kafelku po najechaniu */}
                        {hoveredMessageIndex === index && (
                          <div
                            className={`ml-2 flex items-center justify-center text-xs text-gray-500 ${
                              msg.own ? "text-right" : "text-left"
                            }`}
                          >
                            <div className="bg-gray-200 rounded-xl px-2 py-1">
                              {new Date(msg.sendAt).toLocaleTimeString([], {
                                hour: "2-digit",
                                minute: "2-digit",
                              })}
                            </div>
                          </div>
                        )}
                      </div>
                    ))
                  )}
                  <div ref={messagesEndRef} />
                </div>
              </div>

              <div className="flex-shrink-0 p-4 border-t border-gray-100 bg-gray-50">
                <div className="flex items-center gap-4">
                  <input
                    type="text"
                    value={messageInput}
                    onChange={(e) => setMessageInput(e.target.value)}
                    placeholder="Type a message..."
                    className="flex-1 max-w-[calc(100%-6rem)] border border-gray-300 rounded-2xl px-5 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                    onKeyDown={(e) => {
                      if (e.key === "Enter" && !e.shiftKey) {
                        e.preventDefault();
                        if (messageInput.trim()) {
                          messageApi
                            .sendMessage(activeUser.userId, messageInput)
                            .then((sent) => {
                              setMessages((prev) => [...prev, sent]);
                              setMessageInput("");
                            });
                        }
                      }
                    }}
                  />
                  <button
                    className="flex items-center justify-center w-12 h-12 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-full text-sm transition"
                    onClick={async () => {
                      if (!messageInput.trim()) return;

                      const sent = await messageApi.sendMessage(
                        activeUser.userId,
                        messageInput
                      );
                      setMessages((prev) => [...prev, sent]);
                      setMessageInput("");
                    }}
                  >
                    <FiSend className="w-5 h-5" />
                  </button>
                </div>
              </div>
            </>
          ) : (
            <div className="flex-1 flex items-center justify-center text-gray-500 italic">
              Select a conversation to start messaging
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default MessagePage;
