<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>Notifications</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 700px;
            margin: 40px auto;
            padding: 20px;
            background-color: #f9fafb;
            color: #333;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 24px;
            text-align: center;
        }
        ul.notification-list {
            list-style: none;
            padding: 0;
        }
        ul.notification-list li {
            background: white;
            padding: 15px 20px;
            margin-bottom: 12px;
            border-radius: 8px;
            border-left: 6px solid #4f46e5;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        ul.notification-list li.read {
            border-left-color: #9ca3af;
            background: #f3f4f6;
            color: #6b7280;
        }
        .notif-content {
            flex: 1;
            margin-right: 15px;
        }
        .notif-date {
            font-size: 0.85rem;
            color: #9ca3af;
            white-space: nowrap;
        }
        button.mark-read {
            background: #4f46e5;
            color: white;
            border: none;
            padding: 6px 12px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 0.9rem;
            transition: background 0.3s ease;
        }
        button.mark-read:hover {
            background: #4338ca;
        }
        .no-notifications {
            text-align: center;
            font-style: italic;
            color: #6b7280;
            margin-top: 40px;
        }
    </style>
    <script>
        function markAsRead(notificationId, button) {
            fetch(`notification?action=markRead&notificationId=${notificationId}`, {
                method: 'POST'
            })
            .then(response => {
                if (response.ok) {
                    button.disabled = true;
                    button.textContent = "Read";
                    button.closest('li').classList.add('read');
                } else {
                    alert('Failed to mark notification as read.');
                }
            })
            .catch(() => alert('Error occurred while marking as read.'));
        }
    </script>
</head>
<body>
    <h1>Notifications</h1>
    <c:choose>
        <c:when test="${not empty notifications}">
            <ul class="notification-list">
                <c:forEach var="notification" items="${notifications}">
                    <li class="${notification.isRead ? 'read' : ''}">
                        <div class="notif-content">${notification.content}</div>
                        <div class="notif-date">
                            <fmt:formatDate value="${notification.createdAt}" pattern="MMM dd, yyyy HH:mm" />
                        </div>
                        <button class="mark-read" 
                            onclick="markAsRead(${notification.notificationId}, this)" 
                            ${notification.isRead ? 'disabled' : ''}>
                            ${notification.isRead ? 'Read' : 'Mark as Read'}
                        </button>
                    </li>
                </c:forEach>
            </ul>
        </c:when>
        <c:otherwise>
            <div class="no-notifications">You have no notifications at this time.</div>
        </c:otherwise>
    </c:choose>
</body>
</html>